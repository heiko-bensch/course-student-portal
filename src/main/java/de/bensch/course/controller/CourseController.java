package de.bensch.course.controller;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.entity.Course;
import de.bensch.course.service.CourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static de.bensch.course.controller.UrlMappings.*;

@Controller
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping(COURSE_LIST)
    public String courses(Model model,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(defaultValue = "id,asc") String[] sort) {

        String sortField = sort[0];
        String sortDirection = sort[1];
        Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));
        Page<Course> coursePage;
        if (StringUtils.isBlank(keyword)) {
            coursePage = courseService.findAll(pageable);

        } else {
            coursePage = courseService.findByKeyword(pageable, keyword);
            model.addAttribute("keyword", keyword);
        }
        model.addAttribute("courseList", coursePage.getContent());
        model.addAttribute("currentPage", coursePage.getNumber() + 1);
        model.addAttribute("totalItems", coursePage.getTotalElements());
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
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
