package de.bensch.course.controller;

import de.bensch.course.model.entity.Student;
import de.bensch.course.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static de.bensch.course.controller.UrlMappings.*;

@Slf4j
@Controller
@AllArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @ModelAttribute("urlMappings")
    public UrlMappings urlMappings() {
        //noinspection InstantiationOfUtilityClass
        return new UrlMappings();
    }


    @GetMapping(STUDENT_LIST)
    public String courses(Model model,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false, defaultValue = "all") String selectedGradeLevel,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(defaultValue = "id,asc") String[] sort) {
        String sortField = sort[0];
        String sortDirection = sort[1];
        Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));
        Page<Student> studentPage;
        List<String> gradeLevels = studentService.findGradeLevel();
        if (StringUtils.isBlank(keyword)) {
            if (Objects.equals("all", selectedGradeLevel)) {
                studentPage = studentService.findAll(pageable);
            } else {
                studentPage = studentService.findAll(pageable, selectedGradeLevel);
                model.addAttribute("selectedGradeLevel", selectedGradeLevel);
            }
        } else {
            if (Objects.equals("all", selectedGradeLevel)) {
                studentPage = studentService.findByKeyword(pageable, keyword);

            } else {
                studentPage = studentService.findByKeyword(pageable, selectedGradeLevel, keyword);
                model.addAttribute("selectedGradeLevel", selectedGradeLevel);

            }
            model.addAttribute("keyword", keyword);
        }
        model.addAttribute("studentList", studentPage.getContent());
        model.addAttribute("currentPage", studentPage.getNumber() + 1);
        model.addAttribute("totalItems", studentPage.getTotalElements());
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("gradeLevels", gradeLevels);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        return STUDENT_LIST;
    }

    @GetMapping(STUDENT_CREATE)
    public String createCourse(Model model) {
        model.addAttribute("student", new Student());
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
        return "redirect:" + STUDENT_LIST;
    }

    @GetMapping(STUDENT_DELETE)
    public String deleteStudent(@PathVariable("id") Long id) {
        studentService.delete(id);
        return "redirect:" + STUDENT_LIST;
    }
}
