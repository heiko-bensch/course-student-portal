package de.bensch.course.controller;

import de.bensch.course.model.Course;
import de.bensch.course.model.WeekDay;
import de.bensch.course.service.CourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        //noinspection
        return new UrlMappings();
    }

    @GetMapping(COURSE_LIST)
    public String courses(Model model, @RequestParam(required = false) String keyword) {
        if(StringUtils.isBlank(keyword)){
            model.addAttribute("courseList", courseService.findAll());
        }else {
            model.addAttribute("courseList", courseService.findByKeyword(keyword));
            model.addAttribute("keyword", keyword);
        }
        return COURSE_LIST;
    }

    @GetMapping(COURSE_CREATE)
    public String createCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("allDays", WeekDay.values());
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
        model.addAttribute("allDays", WeekDay.values());
        return COURSE_CREATE;
    }

    @PostMapping(COURSE_EDIT)
    public String editCourseSubmit(@PathVariable("id") Long id, @ModelAttribute Course course) {
        courseService.save(course);
        return "redirect:" + COURSE_LIST;
    }

    @GetMapping(COURSE_DELETE)
    public String deleteCourse(@PathVariable("id") Long id) {

        courseService.delete(id);
        return "redirect:" + COURSE_LIST;
    }

}
