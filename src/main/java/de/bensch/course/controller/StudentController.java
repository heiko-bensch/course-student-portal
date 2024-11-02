package de.bensch.course.controller;

import static de.bensch.course.config.constants.SessionConstants.SEMESTER;
import static de.bensch.course.controller.routing.StudentMappings.STUDENT_CREATE;
import static de.bensch.course.controller.routing.StudentMappings.STUDENT_DELETE;
import static de.bensch.course.controller.routing.StudentMappings.STUDENT_EDIT;
import static de.bensch.course.controller.routing.StudentMappings.STUDENT_LIST;
import static de.bensch.course.controller.routing.StudentMappings.redirect;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.bensch.course.config.constants.SessionConstants;
import de.bensch.course.model.entity.Student;
import de.bensch.course.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@AllArgsConstructor
@SuppressWarnings("squid:S3753")
@SessionAttributes(SessionConstants.SEMESTER)
public class StudentController {
    public static final String MODEL_KEYWORD = "keyword";
    public static final String MODEL_STUDENT_LIST1 = "studentList";
    public static final String MODEL_SELECTED_GRADE_LEVEL = "selectedGradeLevel";
    public static final String MODEL_GRADE_LEVELS = "gradeLevels";
    public static final String MODEL_STUDENT = "student";
    private final StudentService studentService;

    @GetMapping(STUDENT_LIST)
    public String courses(Model model,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false, defaultValue = "all") String selectedGradeLevel,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(defaultValue = "id,asc") String[] sort) {
        String semester = (String) model.getAttribute(SessionConstants.SEMESTER);

        Pageable pageable = PageUtils.createPageable(page, size, sort);
        Page<Student> studentPage;
        List<String> gradeLevels = studentService.findGradeLevel(semester);
        if (StringUtils.isBlank(keyword)) {
            studentPage = getStudentsByGradeLevel(selectedGradeLevel, pageable, semester);
        } else {
            studentPage = getStudentsByKeyword(selectedGradeLevel, keyword, pageable, semester);
            model.addAttribute(MODEL_KEYWORD, keyword);
        }
        PageUtils.addPaginationAttributesToModel(model, studentPage);
        addAttributesToModel(model, studentPage.getContent(), selectedGradeLevel, gradeLevels);
        return STUDENT_LIST;
    }


    private Page<Student> getStudentsByGradeLevel(String selectedGradeLevel, Pageable pageable, String semester) {
        if (Objects.equals("all", selectedGradeLevel)) {
            return studentService.findAllBySemester(pageable, semester);
        } else {
            return studentService.findAllBySemester(pageable, semester, selectedGradeLevel);
        }
    }

    private Page<Student> getStudentsByKeyword(String selectedGradeLevel, String keyword, Pageable pageable, String semester) {
        if (Objects.equals("all", selectedGradeLevel)) {
            return studentService.findBySemesterAndKeyword(pageable, semester, keyword);
        } else {
            return studentService.findBySemesterAndKeyword(pageable, semester, selectedGradeLevel, keyword);
        }
    }

    private void addAttributesToModel(Model model, List<Student> studentPage, String selectedGradeLevel, List<String> gradeLevels) {
        model.addAttribute(MODEL_STUDENT_LIST1, studentPage);
        model.addAttribute(MODEL_SELECTED_GRADE_LEVEL, selectedGradeLevel);
        model.addAttribute(MODEL_GRADE_LEVELS, gradeLevels);
    }

    @GetMapping(STUDENT_CREATE)
    public String createStudent(Model model) {
        model.addAttribute(MODEL_STUDENT, new Student());
        return STUDENT_CREATE;
    }

    @PostMapping(STUDENT_CREATE)
    public String createStudentSubmit(Model model, @ModelAttribute Student student) {
        String result;
        if (student.getId() == null) {
            result = redirect(STUDENT_CREATE);
        } else {
            result = redirect(STUDENT_LIST);
        }
        student.setSemester((String) model.getAttribute(SEMESTER));
        studentService.save(student);
        return result;
    }

    @GetMapping(STUDENT_EDIT)
    public String editStudentForm(@PathVariable("id") Long id, Model model) {
        Student student = studentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID:" + id));
        model.addAttribute(MODEL_STUDENT, student);
        return STUDENT_CREATE;
    }

    @PostMapping(STUDENT_EDIT)
    public String editStudentSubmit(Model model,@PathVariable("id") Long id, @ModelAttribute Student student) {
        student.setSemester((String) model.getAttribute(SEMESTER));
        studentService.save(student);
        return redirect(STUDENT_LIST);
    }

    @GetMapping(STUDENT_DELETE)
    public String deleteStudent(@PathVariable("id") Long id) {
        studentService.delete(id);
        return redirect(STUDENT_LIST);
    }
}
