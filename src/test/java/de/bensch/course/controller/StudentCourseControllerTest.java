package de.bensch.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bensch.course.model.view.StudentCourseSelectionView;
import de.bensch.course.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static de.bensch.course.config.constants.SessionConstants.SEMESTER;
import static de.bensch.course.controller.StudentCourseController.MODEL_GRADE_LEVELS;
import static de.bensch.course.controller.StudentCourseController.MODEL_STUDENT_LIST;
import static de.bensch.course.controller.routing.StudentCoursePaths.URL_STUDENT_COURSE_ASSIGNMENT;
import static de.bensch.course.controller.routing.StudentCoursePaths.URL_STUDENT_COURSE_LIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentCourseController.class)
@Import(TestSecurityConfig.class)
class StudentCourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private StudentCourseSelectionService studentCourseSelectionService;

    @MockBean
    private SemesterService semesterService;

    @MockBean
    private CourseStudentExcelExportService exportService;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void showStudentCourseListPage() throws Exception {
        String semester = "01/2024";
        Pageable pageable = PageRequest.of(0, 10);
        Page<StudentCourseSelectionView> page = new PageImpl<>(List.of(mock(StudentCourseSelectionView.class)), pageable, 0L);

        when(studentService.findGradeLevel(semester)).thenReturn(List.of("1", "2", "3", "4", "5", "6"));
        when(studentCourseSelectionService.findAllByStudentCourseCountByDayOfWeek(any(), eq(semester)))
                .thenReturn(page);
        mockMvc.perform(get(URL_STUDENT_COURSE_LIST)
                        .param("selectedGradeLevel", "all")
                        .param("page", "1")
                        .param("size", "10")
                        .sessionAttr(SEMESTER, semester))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(MODEL_STUDENT_LIST))
                .andExpect(model().attributeExists(MODEL_GRADE_LEVELS))
                .andExpect(model().attribute("pageSize", 10))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("totalItems", 1L))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(view().name(URL_STUDENT_COURSE_LIST));
    }

    @Test
    void showStudentCourseAssignmentForm() throws Exception {
        mockMvc.perform(get("/student-course-assignment/1")
                        .sessionAttr(SEMESTER, "01/2024"))
                .andExpect(status().isOk())
                .andExpect(view().name(URL_STUDENT_COURSE_ASSIGNMENT));
    }

    @Test
    void submitStudentCourseAssignmentForm_noNextElement() throws Exception {

        mockMvc.perform(post(URL_STUDENT_COURSE_ASSIGNMENT)
                        .sessionAttr(SEMESTER, "01/2024")
                        .param("student.id", "12") //model attribute
                        .param("selectedGradeLevel", "10")
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_STUDENT_COURSE_LIST + "?selectedGradeLevel=10"));
    }

    @Test
    void submitStudentCourseAssignmentForm_withNextElement() throws Exception {
        StudentCourseSelectionView mock = mock(StudentCourseSelectionView.class);
        when(mock.getId()).thenReturn(12L);

        when(studentCourseSelectionService.findNextEntry(any(), any(), any()))
                .thenReturn(Optional.of(mock));

        mockMvc.perform(post(URL_STUDENT_COURSE_ASSIGNMENT)
                        .sessionAttr(SEMESTER, "01/2024")
                        .param("student.id", "12") //model attribute
                        .param("selectedGradeLevel", "10")
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_STUDENT_COURSE_ASSIGNMENT + "/12?selectedGradeLevel=10"));
    }

}
