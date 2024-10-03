package de.bensch.course.controller;

import de.bensch.course.model.Course;
import de.bensch.course.model.Student;
import de.bensch.course.model.WeekDay;
import de.bensch.course.service.CourseService;
import de.bensch.course.service.StudenCourseService;
import de.bensch.course.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@AllArgsConstructor
@Slf4j
public class StudentCourseController {

    private final StudentService studentService;

    private final StudenCourseService studenCourseService;

    private final CourseService courseService;


    @GetMapping(UrlMappings.STUDENT_COURSE_ASSIGNMENT)
    public String searchStudents(Model model) {

        Iterable<Course> monday = courseService.findByDayOfWeekday(WeekDay.Monday);
        Iterable<Course> tuesday = courseService.findByDayOfWeekday(WeekDay.Tuesday);
        Iterable<Course> wednesday = courseService.findByDayOfWeekday(WeekDay.Wednesday);
        Iterable<Course> thursday = courseService.findByDayOfWeekday(WeekDay.Thursday);
        Optional<Student> student = studentService.findById(1L);
        model.addAttribute("student", student.get());
        model.addAttribute("mondayCourseList", monday);
        model.addAttribute("tuesdayCourseList", tuesday);
        model.addAttribute("wednesdayCourseList", wednesday);
        model.addAttribute("thursdayCourseList", thursday);

//        List<Student> students = studentService.findStudentsByClassNameAndNameContaining(className, query);
//        List<String> classNames = studentService.getAllClassNames(); // Fetch all class names
//       // Iterable<Course> courses = courseService.findAll(pageable); // Fetch all courses for dropdown
//
//        model.addAttribute("students", students);
//        model.addAttribute("classNames", classNames);
//        model.addAttribute("selectedStudent", null); // Clear selected student
//      //  model.addAttribute("courses", courses);

        return UrlMappings.STUDENT_COURSE_ASSIGNMENT;
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

        return "student-list";
    }

    @PostMapping("/assignCourse")
    public String assignCourse(@RequestParam Long studentId, @RequestParam Long courseId, Model model) {
//        studentService.assignCourseToStudent(studentId, courseId); // Implement this method in your service
        return "redirect:/search"; // Redirect to search results or wherever needed
    }


}
