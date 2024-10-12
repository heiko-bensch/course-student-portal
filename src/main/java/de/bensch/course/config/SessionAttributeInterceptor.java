package de.bensch.course.config;


import de.bensch.course.config.constants.SessionConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionAttributeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        if (session.getAttribute(SessionConstants.SEMESTER) == null) {
            // Initialisierung der Session-Attribute
            session.setAttribute(SessionConstants.SEMESTER, "01/2024");
        }
        return true;
    }
}
