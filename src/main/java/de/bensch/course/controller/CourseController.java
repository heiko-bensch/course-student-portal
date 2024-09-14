package de.bensch.course.controller;

import de.bensch.course.model.Course;
import de.bensch.course.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CourseController {
    private static String COURSE_LIST = "course-list";
    private static String COURSE_CREATE = "course-create";

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public String courses(Model model) {
        model.addAttribute("courseList", courseService.findAll());
        return COURSE_LIST;
    }

    @GetMapping("/courseCreate")
    public String createCourseForm(Model model) {
        model.addAttribute("course", new Course(null, null, null, null, null, null));
        return COURSE_CREATE;
    }

    @PostMapping("/courseCreate")
    public String createCourseSubmit(@ModelAttribute Course course) {
        courseService.save(course);
        return "redirect:"+COURSE_LIST;
    }

    @GetMapping("/courseEdit/{id}")
    public String editCourseForm(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID:" + id));
        model.addAttribute("course", course);
        return COURSE_CREATE;
    }

    @PostMapping("/courseEdit/{id}")
    public String editCourseSubmit(@PathVariable("id") Long id, @ModelAttribute Course course) {
        courseService.save(course);
        return "redirect:/"+COURSE_LIST;
    }

    @GetMapping("/courseDelete/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID:" + id));
        courseService.delete(course);
        return "redirect:/"+COURSE_LIST;
    }

}
