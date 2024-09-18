package de.bensch.course.controller;

import de.bensch.course.model.Student;
import de.bensch.course.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static de.bensch.course.controller.UrlMappings.*;

@Controller
public class StudentController {

    private final StudentService studentService;

    @ModelAttribute("urlMappings")
    public UrlMappings urlMappings() {
        //noinspection InstantiationOfUtilityClass
        return new UrlMappings();
    }

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping(STUDENT_LIST)
    public String courses(Model model) {
        model.addAttribute("studentList", studentService.findAll());
        return STUDENT_LIST;
    }

    @GetMapping(STUDENT_CREATE)
    public String createCourse(Model model) {
        model.addAttribute("student", new Student(null, null, 0, null));
        return STUDENT_CREATE;
    }

    @PostMapping(STUDENT_CREATE)
    public String createStudentSubmit(@ModelAttribute Student student) {
        String result;
        if (student.getId() == null) {
            result = "redirect:" + STUDENT_CREATE;
        } else {
            result = "redirect:" + STUDENT_LIST;
        }
        studentService.save(student);
        return result;
    }

    @GetMapping(STUDENT_EDIT)
    public String editStudentForm(@PathVariable("id") Long id, Model model) {
        Student student = studentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID:" + id));
        model.addAttribute("student", student);
        return STUDENT_CREATE;
    }

    @PostMapping(STUDENT_EDIT)
    public String editStudentSubmit(@PathVariable("id") Long id, @ModelAttribute Student student) {
        studentService.save(student);
        return "redirect:"+STUDENT_LIST;
    }

    @GetMapping(STUDENT_DELETE)
    public String deleteStudent(@PathVariable("id") Long id) {
        Student student = studentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID:" + id));
        studentService.delete(student);
        return "redirect:"+STUDENT_LIST;
    }
}
