package de.bensch.course.controller;

import static de.bensch.course.config.constants.SessionConstants.SEMESTER;
import static de.bensch.course.controller.routing.CourseMappings.COURSE_CREATE;
import static de.bensch.course.controller.routing.CourseMappings.COURSE_DELETE;
import static de.bensch.course.controller.routing.CourseMappings.COURSE_EDIT;
import static de.bensch.course.controller.routing.CourseMappings.COURSE_LIST;
import static de.bensch.course.controller.routing.CourseMappings.redirect;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.entity.Course;
import de.bensch.course.service.CourseService;

@Controller
@SuppressWarnings("squid:S3753")
@SessionAttributes(SEMESTER)
public class CourseController {
    public static final String MODEL_KEYWORD = "keyword";
    public static final String MODEL_COURSE = "course";
    public static final String MODEL_ALL_DAYS = "allDays";
    public static final String MODEL_COURSE_LIST = "courseList";

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
            model.addAttribute(MODEL_KEYWORD, keyword);
        }
        Utils.addPaginationAttributesToModel(model, coursePage);
        model.addAttribute(MODEL_COURSE_LIST, coursePage.getContent());
        return COURSE_LIST;
    }

    @GetMapping(COURSE_CREATE)
    public String createCourseForm(Model model) {
        model.addAttribute(MODEL_COURSE, new Course());
        model.addAttribute(MODEL_ALL_DAYS, WeekDay.values());
        return COURSE_CREATE;
    }

    @PostMapping(COURSE_CREATE)
    public String createCourseSubmit(Model model, @ModelAttribute Course course) {
        String result;
        if (course.getId() == null) {
            result = redirect(COURSE_CREATE);
        } else {
            result = redirect(COURSE_LIST);
        }
        course.setSemester((String) model.getAttribute(SEMESTER));
        courseService.save(course);
        return result;
    }

    @GetMapping(COURSE_EDIT)
    public String editCourseForm(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID:" + id));
        model.addAttribute(MODEL_COURSE, course);
        model.addAttribute(MODEL_ALL_DAYS, WeekDay.values());
        return COURSE_CREATE;
    }

    @PostMapping(COURSE_EDIT)
    public String editCourseSubmit(Model model, @PathVariable("id") Long id, @ModelAttribute Course course) {
        course.setSemester((String) model.getAttribute(SEMESTER));
        courseService.save(course);
        return redirect(COURSE_LIST);
    }

    @GetMapping(COURSE_DELETE)
    public String deleteCourse(@PathVariable("id") Long id) {

        courseService.delete(id);
        return redirect(COURSE_LIST);
    }

}
