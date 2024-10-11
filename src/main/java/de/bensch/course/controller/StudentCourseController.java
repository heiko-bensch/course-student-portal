package de.bensch.course.controller;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.dto.StudentCourseSelectionDTO;
import de.bensch.course.model.entity.Course;
import de.bensch.course.model.entity.StudentCourseSelection;
import de.bensch.course.model.view.StudentCourseSelectionView;
import de.bensch.course.service.CourseService;
import de.bensch.course.service.CourseStudentExcelExportService;
import de.bensch.course.service.StudentCourseSelectionService;
import de.bensch.course.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static de.bensch.course.controller.UrlMappings.STUDENT_COURSE_EXPORT;
import static de.bensch.course.controller.UrlMappings.STUDENT_COURSE_LIST;

@Controller
@AllArgsConstructor
@Slf4j
public class StudentCourseController {

    private final StudentService studentService;

    private final StudentCourseSelectionService studentCourseSelectionService;

    private final CourseStudentExcelExportService exportService;

    private final CourseService courseService;


    @GetMapping(UrlMappings.STUDENT_COURSE_ASSIGNMENT + "/{id}")
    public String searchStudents(@PathVariable("id") Long id, @RequestParam(required = false, defaultValue = "all") String selectedGradeLevel, Model model) {

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
        model.addAttribute("selectedGradeLevel", selectedGradeLevel);

        return UrlMappings.STUDENT_COURSE_ASSIGNMENT;
    }

    @PostMapping(UrlMappings.STUDENT_COURSE_ASSIGNMENT)
    public String torte(@ModelAttribute StudentCourseSelectionDTO courseSelection,
                        @RequestParam(name = "selectedGradeLevel", defaultValue = "all", required = false) String selectedGradeLevel,
                        RedirectAttributes redirectAttributes) {
        studentCourseSelectionService.saveStudentCourseSelection(courseSelection);
        // Zum nächsten Eintrag weiterleiten
        Optional<StudentCourseSelectionView> nextEntry = studentCourseSelectionService
                .findNextEntry(courseSelection.getStudent().getId(), selectedGradeLevel);
        redirectAttributes.addAttribute("selectedGradeLevel", selectedGradeLevel);
        if (nextEntry.isPresent()) {
            redirectAttributes.addAttribute("id", nextEntry.get().getId());
            return "redirect:" + UrlMappings.STUDENT_COURSE_ASSIGNMENT + "/{id}?selectedGradeLevel={selectedGradeLevel}";
        } else {
            return "redirect:" + STUDENT_COURSE_LIST + "?selectedGradeLevel={selectedGradeLevel}";
        }

    }


    @GetMapping(STUDENT_COURSE_LIST)
    public String studentCourseList(Model model,
                                    @RequestParam(required = false, defaultValue = "all") String selectedGradeLevel,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size) {


        List<String> gradeLevels = studentService.findGradeLevel();

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<StudentCourseSelectionView> studentCourseSelectionView;
        if (Objects.equals("all", selectedGradeLevel)) {
            studentCourseSelectionView = studentCourseSelectionService.findAllByStudentCourseCountByDayOfWeek(pageable);
        } else {
            studentCourseSelectionView = studentCourseSelectionService.findAllByStudentCourseCountByDayOfWeek(pageable, selectedGradeLevel);
            model.addAttribute("selectedGradeLevel", selectedGradeLevel);
        }


        model.addAttribute("studentList", studentCourseSelectionView);
        model.addAttribute("currentPage", studentCourseSelectionView.getNumber() + 1);
        model.addAttribute("totalItems", studentCourseSelectionView.getTotalElements());
        model.addAttribute("totalPages", studentCourseSelectionView.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("gradeLevels", gradeLevels);
        return STUDENT_COURSE_LIST;
    }

    @GetMapping(STUDENT_COURSE_EXPORT)
    public ResponseEntity<byte[]> exportExcel() throws IOException {
        Collection<StudentCourseSelection> selectionList = studentCourseSelectionService.findAll();
        byte[] data = exportService.export(selectionList);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }


}
