package de.bensch.course.controller;

import de.bensch.course.model.Course;
import de.bensch.course.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static de.bensch.course.controller.UrlMappings.*;

@Controller
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @ModelAttribute("urlMappings")
    public UrlMappings urlMappings() {
        //noinspection InstantiationOfUtilityClass
        return new UrlMappings();
    }


    @GetMapping(COURSE_LIST)
    public String courses(Model model) {
        model.addAttribute("courseList", courseService.findAll());
        return COURSE_LIST;
    }

    @GetMapping(COURSE_CREATE)
    public String createCourseForm(Model model) {
        model.addAttribute("course", new Course(null, null, null, null, null, null));
        model.addAttribute("allDays", List.of("Montag", "Dienstag", "Mittwoch", "Donnerstag"));
        return COURSE_CREATE;
    }

    @PostMapping(COURSE_CREATE)
    public String createCourseSubmit(@ModelAttribute Course course) {
        String result;
        if (course.getId() == null) {
            result = "redirect:" + COURSE_CREATE;
        } else {
            result = "redirect:" + COURSE_LIST;
        }
        courseService.save(course);
        return result;
    }

    @GetMapping(COURSE_EDIT)
    public String editCourseForm(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID:" + id));
        model.addAttribute("course", course);
        model.addAttribute("allDays", List.of("Montag", "Dienstag", "Mittwoch", "Donnerstag"));
        return COURSE_CREATE;
    }

    @PostMapping(COURSE_EDIT)
    public String editCourseSubmit(@PathVariable("id") Long id, @ModelAttribute Course course) {
        courseService.save(course);
        return "redirect:" + COURSE_LIST;
    }

    @GetMapping(COURSE_DELETE)
    public String deleteCourse(@PathVariable("id") Long id) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID:" + id));
        courseService.delete(course);
        return "redirect:" + COURSE_LIST;
    }

}
