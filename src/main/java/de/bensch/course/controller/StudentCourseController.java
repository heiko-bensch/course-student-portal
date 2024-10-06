package de.bensch.course.controller;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.dto.StudentCourseSelectionDTO;
import de.bensch.course.model.entity.Course;
import de.bensch.course.model.entity.Student;
import de.bensch.course.service.CourseService;
import de.bensch.course.service.StudentCourseSelectionService;
import de.bensch.course.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static de.bensch.course.controller.UrlMappings.COURSE_LIST;

@Controller
@AllArgsConstructor
@Slf4j
public class StudentCourseController {

    private final StudentService studentService;

    private final StudentCourseSelectionService studentCourseSelectionService;

    private final CourseService courseService;


    @GetMapping(UrlMappings.STUDENT_COURSE_ASSIGNMENT)
    public String searchStudents(Model model, @RequestParam(defaultValue = "1") Long id) {

        Iterable<Course> monday = courseService.findByDayOfWeekday(WeekDay.Monday);
        Iterable<Course> tuesday = courseService.findByDayOfWeekday(WeekDay.Tuesday);
        Iterable<Course> wednesday = courseService.findByDayOfWeekday(WeekDay.Wednesday);
        Iterable<Course> thursday = courseService.findByDayOfWeekday(WeekDay.Thursday);
        Optional<StudentCourseSelectionDTO> courseSelection = studentCourseSelectionService.findByStudentId(id);


        if (courseSelection.isPresent()) {
            model.addAttribute("studentCourse", courseSelection.get());
        } else {
            model.addAttribute("studentCourse", new StudentCourseSelectionDTO());
        }

        model.addAttribute("mondayCourseList", monday);
        model.addAttribute("tuesdayCourseList", tuesday);
        model.addAttribute("wednesdayCourseList", wednesday);
        model.addAttribute("thursdayCourseList", thursday);

        return UrlMappings.STUDENT_COURSE_ASSIGNMENT;
    }

    @PostMapping(UrlMappings.STUDENT_COURSE_ASSIGNMENT)
    public String torte(@ModelAttribute StudentCourseSelectionDTO courseSelection) {
        studentCourseSelectionService.saveStudentCourseSelection(courseSelection);
        //return UrlMappings.STUDENT_COURSE_ASSIGNMENT;
        return "redirect:" + COURSE_LIST;
    }

    @GetMapping("/student/details/{id}")
    public String viewStudentDetails(@PathVariable Long id, Model model) {
        Optional<Student> student = studentService.findById(id);
        //Iterable<Course> courses = courseService.findAll(pageable); // Fetch all courses for dropdown

        model.addAttribute("selectedStudent", student.get());
        //model.addAttribute("courses", courses);

//        // Reuse classNames and search results to maintain state
//        model.addAttribute("students", studentService.findStudentsByClassNameAndNameContaining(student.get()
//                .getClassName(), ""));
//        model.addAttribute("classNames", studentService.getAllClassNames());

        return UrlMappings.STUDENT_COURSE_ASSIGNMENT;
    }

    @PostMapping("/assignCourse")
    public String assignCourse(@RequestParam Long studentId, @RequestParam Long courseId, Model model) {
        return "redirect:/search"; // Redirect to search results or wherever needed
    }


}
