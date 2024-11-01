package de.bensch.course.controller;

import de.bensch.course.controller.routing.CourseMappings;
import de.bensch.course.controller.routing.StudentMappings;
import de.bensch.course.controller.routing.StudentCourseMappings;
import de.bensch.course.service.SemesterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@AllArgsConstructor
public class GlobalModelAttributes {
    private SemesterService semesterService;

    @SuppressWarnings("InstantiationOfUtilityClass")
    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("studentCourseMappings", new StudentCourseMappings());
        model.addAttribute("courseMappings", new CourseMappings());
        model.addAttribute("studentMappings", new StudentMappings());
        model.addAttribute("semesterList", semesterService.findAllSemester());
    }
}
