package de.bensch.course.controller;

import static de.bensch.course.config.constants.SessionConstants.SEMESTER;
import static de.bensch.course.controller.routing.CoursePaths.URL_COURSE_CREATE;
import static de.bensch.course.controller.routing.CoursePaths.URL_COURSE_LIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

import de.bensch.course.model.entity.Course;
import de.bensch.course.service.CourseService;
import de.bensch.course.service.SemesterService;


@WebMvcTest(CourseController.class)
@Import(TestSecurityConfig.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private SemesterService semesterService;


    @Test
    void showCourseListPage_withSemester_withoutKeyword() throws Exception {
        String semester = "01/2024";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> page = new PageImpl<>(List.of(new Course()), pageable, 0);

        when(courseService.findBySemester(any(), any())).thenReturn(page);

        // Perform GET request
        mockMvc.perform(get(URL_COURSE_LIST)
                        .param("page", "1")
                        .param("size", "10")
                        .sessionAttr(SEMESTER, semester))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("courseList"))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("totalItems", 1L))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("pageSize", 10))
                .andExpect(view().name(URL_COURSE_LIST));
    }

    @Test
    void showCourseListPage_withSemester_withKeyword() throws Exception {
        String semester = "01/2024";
        String keyword = "keyword";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> page = new PageImpl<>(List.of(new Course()), pageable, 0);

        when(courseService.findBySemesterKeyword(any(), any(), any())).thenReturn(page);

        // Perform GET request
        mockMvc.perform(get(URL_COURSE_LIST)
                        .param("page", "1")
                        .param("size", "10")
                        .param("keyword", keyword)
                        .sessionAttr(SEMESTER, semester))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("courseList"))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("totalItems", 1L))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("pageSize", 10))
                .andExpect(model().attribute("keyword", keyword))
                .andExpect(view().name(URL_COURSE_LIST));
    }

    @Test
    void showCreateCourseForm() throws Exception {
        mockMvc.perform(get(URL_COURSE_CREATE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("allDays"))
                .andExpect(view().name(URL_COURSE_CREATE));
    }

    @Test
    void submitCreateCourseForm_NewCourse_ShouldRedirectToCreate() throws Exception {
        Course course = new Course();
        course.setId(null);

        mockMvc.perform(post(URL_COURSE_CREATE)
                        .sessionAttr(SEMESTER, "01/2024")
                        .flashAttr("course", course))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_COURSE_CREATE));

        // Verify that save was called
        verify(courseService).save(any(Course.class));
    }

    @Test
    void submitCreateCourseForm_ExistingCourse_ShouldRedirectToList() throws Exception {
        Course course = new Course();
        course.setId(1L);

        mockMvc.perform(post(URL_COURSE_CREATE)
                        .sessionAttr(SEMESTER, "01/2024")
                        .flashAttr("course", course))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_COURSE_LIST));

        verify(courseService).save(any(Course.class));
    }

    @Test
    void showEditCourseForm() throws Exception {
        Course course = new Course();
        course.setId(1L);
        when(courseService.findById(1L)).thenReturn(java.util.Optional.of(course));

        mockMvc.perform(get("/course-edit/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("allDays"))
                .andExpect(view().name(URL_COURSE_CREATE));
        verify(courseService).findById(1L);
    }

    @Test
    void submitEditCourse() throws Exception {
        Course course = new Course();
        course.setId(1L);

        mockMvc.perform(post("/course-edit/1")
                        .sessionAttr(SEMESTER, "01/2024")
                        .flashAttr("course", course))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_COURSE_LIST));

        verify(courseService).save(any(Course.class));
    }

    @Test
    void deleteCourse() throws Exception {
        mockMvc.perform(get("/course-delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_COURSE_LIST));

        verify(courseService).delete(1L);
    }

}
