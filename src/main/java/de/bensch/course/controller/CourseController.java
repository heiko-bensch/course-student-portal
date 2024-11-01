package de.bensch.course.controller;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.entity.Course;
import de.bensch.course.service.CourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static de.bensch.course.config.constants.SessionConstants.SEMESTER;
import static de.bensch.course.controller.routing.CourseMappings.*;

@Controller
@SuppressWarnings("squid:S3753")
@SessionAttributes(SEMESTER)
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping(value = COURSE_LIST)
    public String courses(Model model, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id,asc") String[] sort) {

        String semester = (String) model.getAttribute(SEMESTER);

        Pageable pageable = Utils.createPageable(page, size, sort);

        Page<Course> coursePage;
        if (StringUtils.isBlank(keyword)) {
            coursePage = courseService.findBySemester(pageable, semester);

        } else {
            coursePage = courseService.findBySemesterKeyword(pageable, semester, keyword);
            model.addAttribute("keyword", keyword);
        }
        Utils.addPaginationAttributesToModel(model, coursePage);
        model.addAttribute("courseList", coursePage.getContent());
        return COURSE_LIST;
    }

    @GetMapping(COURSE_CREATE)
    public String createCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("allDays", WeekDay.values());
        return COURSE_CREATE;
    }

    @PostMapping(COURSE_CREATE)
    public String createCourseSubmit(Model model, @ModelAttribute Course course) {
        String semester = (String) model.getAttribute(SEMESTER);
        String result;
        course.setSemester(semester);
        if (course.getId() == null) {
            result = redirect(COURSE_CREATE);
        } else {
            result = redirect(COURSE_LIST);
        }
        courseService.save(course);
        return result;
    }

    @GetMapping(COURSE_EDIT)
    public String editCourseForm(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID:" + id));
        model.addAttribute("course", course);
        model.addAttribute("allDays", WeekDay.values());
        return COURSE_CREATE;
    }

    @PostMapping(COURSE_EDIT)
    public String editCourseSubmit(@PathVariable("id") Long id, @ModelAttribute Course course) {
        courseService.save(course);
        return redirect( COURSE_LIST);
    }

    @GetMapping(COURSE_DELETE)
    public String deleteCourse(@PathVariable("id") Long id) {

        courseService.delete(id);
        return redirect(COURSE_LIST);
    }

}
