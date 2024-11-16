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

import static de.bensch.course.config.constants.SessionConstants.SEMESTER;
import static de.bensch.course.controller.routing.StudentCoursePaths.*;

@Controller
@AllArgsConstructor
@Slf4j
@SuppressWarnings("squid:S3753")
@SessionAttributes(SEMESTER)
public class StudentCourseController {

    public static final String SELECTED_GRADE_LEVEL = "selectedGradeLevel";

    public static final String MODEL_STUDENT_COURSE = "studentCourse";

    public static final String MODEL_MONDAY_COURSE_LIST = "mondayCourseList";

    public static final String MODEL_TUESDAY_COURSE_LIST = "tuesdayCourseList";

    public static final String MODEL_WEDNESDAY_COURSE_LIST = "wednesdayCourseList";

    public static final String MODEL_THURSDAY_COURSE_LIST = "thursdayCourseList";

    public static final String MODEL_STUDENT_LIST = "studentList";

    public static final String MODEL_GRADE_LEVELS = "gradeLevels";

    private final StudentService studentService;

    private final StudentCourseSelectionService studentCourseSelectionService;

    private final CourseStudentExcelExportService exportService;

    private final CourseService courseService;

    @GetMapping(URL_STUDENT_COURSE_LIST)
    public String showStudentCourseListPage(Model model,
                                            @RequestParam(required = false, defaultValue = "all") String selectedGradeLevel,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size) {


        String semester = (String) model.getAttribute(SEMESTER);
        List<String> gradeLevels = studentService.findGradeLevel(semester);

        Pageable pageable = PageUtils.createPageable(page, size);

        Page<StudentCourseSelectionView> studentCourseSelectionView;
        if (Objects.equals("all", selectedGradeLevel)) {
            studentCourseSelectionView = studentCourseSelectionService.findAllByStudentCourseCountByDayOfWeek(pageable, semester);
        } else {
            studentCourseSelectionView = studentCourseSelectionService.findAllByStudentCourseCountByDayOfWeek(pageable, semester, selectedGradeLevel);
            model.addAttribute(SELECTED_GRADE_LEVEL, selectedGradeLevel);
        }

        PageUtils.addPaginationAttributesToModel(model, studentCourseSelectionView);

        model.addAttribute(MODEL_STUDENT_LIST, studentCourseSelectionView);
        model.addAttribute(MODEL_GRADE_LEVELS, gradeLevels);
        return URL_STUDENT_COURSE_LIST;
    }

    @GetMapping(URL_STUDENT_COURSE_ASSIGNMENT + "/{id}")
    public String showStudentCourseAssignmentForm(Model model, @PathVariable("id") Long id,
                                                  @RequestParam(required = false, defaultValue = "all") String selectedGradeLevel) {
        String semester = (String) model.getAttribute(SEMESTER);
        semester = Objects.requireNonNullElse(semester, "01/2024");
        Iterable<Course> monday = courseService.findBySemesterAndDayOfWeek(semester, WeekDay.Monday);
        Iterable<Course> tuesday = courseService.findBySemesterAndDayOfWeek(semester, WeekDay.Tuesday);
        Iterable<Course> wednesday = courseService.findBySemesterAndDayOfWeek(semester, WeekDay.Wednesday);
        Iterable<Course> thursday = courseService.findBySemesterAndDayOfWeek(semester, WeekDay.Thursday);

        Optional<StudentCourseSelectionDTO> courseSelection = studentCourseSelectionService.findByStudentId(id);


        if (courseSelection.isPresent()) {
            model.addAttribute(MODEL_STUDENT_COURSE, courseSelection.get());
        } else {
            model.addAttribute(MODEL_STUDENT_COURSE, new StudentCourseSelectionDTO());
        }

        model.addAttribute(MODEL_MONDAY_COURSE_LIST, monday);
        model.addAttribute(MODEL_TUESDAY_COURSE_LIST, tuesday);
        model.addAttribute(MODEL_WEDNESDAY_COURSE_LIST, wednesday);
        model.addAttribute(MODEL_THURSDAY_COURSE_LIST, thursday);
        model.addAttribute(SELECTED_GRADE_LEVEL, selectedGradeLevel);

        return URL_STUDENT_COURSE_ASSIGNMENT;
    }

    @PostMapping(URL_STUDENT_COURSE_ASSIGNMENT)
    public String submitStudentCourseAssignmentForm(Model model, @ModelAttribute StudentCourseSelectionDTO courseSelection,
                                                    @RequestParam(name = SELECTED_GRADE_LEVEL, defaultValue = "all", required = false) String selectedGradeLevel,
                                                    RedirectAttributes redirectAttributes) {
        String semester = (String) model.getAttribute(SEMESTER);
        studentCourseSelectionService.saveStudentCourseSelection(courseSelection);
        // Zum nächsten Eintrag weiterleiten
        Optional<StudentCourseSelectionView> nextEntry = studentCourseSelectionService
                .findNextEntry(courseSelection.getStudent().getId(), semester, selectedGradeLevel);
        redirectAttributes.addAttribute(SELECTED_GRADE_LEVEL, selectedGradeLevel);
        if (nextEntry.isPresent()) {
            redirectAttributes.addAttribute("id", nextEntry.get().getId());
            return "redirect:" + URL_STUDENT_COURSE_ASSIGNMENT + "/{id}?selectedGradeLevel={selectedGradeLevel}";
        } else {
            return "redirect:" + URL_STUDENT_COURSE_LIST + "?selectedGradeLevel={selectedGradeLevel}";
        }

    }


    @GetMapping(URL_STUDENT_COURSE_EXPORT)
    public ResponseEntity<byte[]> exportExcel(Model model) throws IOException {
        String semester = (String) model.getAttribute(SEMESTER);
        Collection<StudentCourseSelection> selectionList = studentCourseSelectionService.findBySemester(semester);
        byte[] data = exportService.export(selectionList);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }

}
