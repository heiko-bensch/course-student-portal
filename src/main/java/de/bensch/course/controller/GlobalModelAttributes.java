package de.bensch.course.controller;

import de.bensch.course.controller.routing.CoursePaths;
import de.bensch.course.controller.routing.StudentCoursePaths;
import de.bensch.course.controller.routing.StudentPaths;
import de.bensch.course.service.SemesterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@AllArgsConstructor
public class GlobalModelAttributes {
    public static final String MODEL_REQUEST_URI = "requestURI";

    public static final String MODEL_STUDENT_COURSE_MAPPINGS = "studentCourseMappings";

    public static final String MODEL_COURSE_MAPPINGS = "courseMappings";

    public static final String MODEL_STUDENT_MAPPINGS = "studentMappings";

    public static final String MODEL_SEMESTER_LIST = "semesterList";

    private SemesterService semesterService;

    @SuppressWarnings("InstantiationOfUtilityClass")
    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        model.addAttribute(MODEL_REQUEST_URI, request.getRequestURI());

        model.addAttribute(MODEL_STUDENT_COURSE_MAPPINGS, new StudentCoursePaths()); //NOSONAR
        model.addAttribute(MODEL_COURSE_MAPPINGS, new CoursePaths());
        model.addAttribute(MODEL_STUDENT_MAPPINGS, new StudentPaths()); //NOSONAR
        model.addAttribute(MODEL_SEMESTER_LIST, semesterService.findAllSemester());
    }
}
