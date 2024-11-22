package de.bensch.course.controller;

import static de.bensch.course.config.constants.SessionConstants.SEMESTER;
import static de.bensch.course.controller.routing.StudentPaths.URL_STUDENT_CREATE;
import static de.bensch.course.controller.routing.StudentPaths.URL_STUDENT_LIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

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

import de.bensch.course.model.entity.Student;
import de.bensch.course.service.SemesterService;
import de.bensch.course.service.StudentService;

@WebMvcTest(StudentController.class)
@Import(TestSecurityConfig.class)
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private SemesterService semesterService;

    @Test
    void showStudentListPage() throws Exception {
        String semester = "01/2024";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Student> page = new PageImpl<>(List.of(new Student()), pageable, 0L);

        when(studentService.findGradeLevel(semester)).thenReturn(List.of("1", "2", "3", "4", "5", "6"));
        when(studentService.findAllBySemester(any(), eq(semester)))
                .thenReturn(page);

        mockMvc.perform(get(URL_STUDENT_LIST)
                        .param("page", "1")
                        .param("size", "10")
                        .sessionAttr(SEMESTER, semester))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("studentList"))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("totalItems", 1L))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("pageSize", 10))
                .andExpect(view().name(URL_STUDENT_LIST));
    }

    @Test
    void showCreateStudentForm() throws Exception {
        mockMvc.perform(get(URL_STUDENT_CREATE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("student"))
                .andExpect(view().name(URL_STUDENT_CREATE));
    }

    @Test
    void submitCreateStudentForm_NewStudent() throws Exception {
        Student student = new Student();

        mockMvc.perform(post(URL_STUDENT_CREATE)
                        .sessionAttr(SEMESTER, "01/2024")
                        .flashAttr("student", student))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_STUDENT_CREATE));
        verify(studentService).save(any(Student.class));
    }

    @Test
    void showEditStudentForm() throws Exception {
        Student student = new Student();
        student.setId(1L);
        when(studentService.findById(1L)).thenReturn(java.util.Optional.of(student));

        mockMvc.perform(get("/student-edit/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("student"))
                .andExpect(view().name(URL_STUDENT_CREATE));
    }

    @Test
    void submitEditStudentForm() throws Exception {
        Student student = new Student();

        mockMvc.perform(post("/student-edit/1")
                        .sessionAttr(SEMESTER, "01/2024")
                        .flashAttr("student", student))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_STUDENT_LIST));
        verify(studentService).save(any(Student.class));
    }

    @Test
    void submitCreateStudentForm_EditStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);

        mockMvc.perform(post(URL_STUDENT_CREATE)
                        .sessionAttr(SEMESTER, "01/2024")
                        .flashAttr("student", student))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_STUDENT_LIST));
        verify(studentService).save(any(Student.class));
    }

    @Test
    void deleteStudent() throws Exception {
        mockMvc.perform(delete("/student-delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_STUDENT_LIST));
        verify(studentService).delete(1L);
    }

}
