package de.bensch.course.config;


import de.bensch.course.config.constants.SessionConstants;
import de.bensch.course.service.SemesterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@AllArgsConstructor
public class SessionAttributeInterceptor implements HandlerInterceptor {
    SemesterService semesterService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        if (session.getAttribute(SessionConstants.SEMESTER) == null) {
            // Initialisierung der Session-Attribute
            session.setAttribute(SessionConstants.SEMESTER, semesterService.getCurrentSemester());
        }
        return true;
    }
}
