package de.bensch.course.controller;

import de.bensch.course.model.Student;
import de.bensch.course.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public String courses(Model model) {
        model.addAttribute("studentsList", studentService.findAll());
        return "students";
    }

    @GetMapping("/studentCreate")
    public String createCourseForm(Model model) {
        model.addAttribute("student", new Student(null, null, 0, null));
        return "studentForm";
    }

    @PostMapping("/studentCreate")
    public String createStudentSubmit(@ModelAttribute Student student) {
        studentService.save(student);
        return "redirect:/students";
    }

    @GetMapping("/studentEdit/{id}")
    public String editStudentForm(@PathVariable("id") Long id, Model model) {
        Student student = studentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID:" + id));
        model.addAttribute("student", student);
        return "studentForm";
    }

    @PostMapping("/studentEdit/{id}")
    public String editStudentSubmit(@PathVariable("id") Long id, @ModelAttribute Student student) {
        studentService.save(student);
        return "redirect:/students";
    }

    @GetMapping("/studentDelete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        Student student = studentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID:" + id));
        studentService.delete(student);
        return "redirect:/students";
    }
}
