package de.bensch.course.controller;

import de.bensch.course.model.Course;
import de.bensch.course.model.Student;
import de.bensch.course.service.CourseService;
import de.bensch.course.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class StudentCourseController {

    private final StudentService studentService;

    private final CourseService courseService;

    public StudentCourseController(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping("/search")
    public String searchStudents(@RequestParam String className, @RequestParam String query, Model model) {
        List<Student> students = studentService.findStudentsByClassNameAndNameContaining(className, query);
        List<String> classNames = studentService.getAllClassNames(); // Fetch all class names
        Iterable<Course> courses = courseService.findAll(); // Fetch all courses for dropdown

        model.addAttribute("students", students);
        model.addAttribute("classNames", classNames);
        model.addAttribute("selectedStudent", null); // Clear selected student
        model.addAttribute("courses", courses);

        return "student-list";
    }

    @GetMapping("/student/details/{id}")
    public String viewStudentDetails(@PathVariable Long id, Model model) {
        Optional<Student> student = studentService.findById(id);
        Iterable<Course> courses = courseService.findAll(); // Fetch all courses for dropdown

        model.addAttribute("selectedStudent", student.get());
        model.addAttribute("courses", courses);

        // Reuse classNames and search results to maintain state
        model.addAttribute("students", studentService.findStudentsByClassNameAndNameContaining(student.get()
                .getClassName(), ""));
        model.addAttribute("classNames", studentService.getAllClassNames());

        return "student-list";
    }

    @PostMapping("/assignCourse")
    public String assignCourse(@RequestParam Long studentId, @RequestParam Long courseId, Model model) {
        studentService.assignCourseToStudent(studentId, courseId); // Implement this method in your service
        return "redirect:/search"; // Redirect to search results or wherever needed
    }
}
