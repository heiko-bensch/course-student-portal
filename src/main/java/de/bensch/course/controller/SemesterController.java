package de.bensch.course.controller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import static de.bensch.course.config.constants.SessionConstants.SEMESTER;

@Controller
@SessionAttributes(SEMESTER)
@AllArgsConstructor
public class SemesterController {


    // Auswahl und Speicherung des Mandanten in der Session
    @GetMapping("/selectSemester")
    public String selectedSemester(Model model, @RequestParam(SEMESTER) String semester,
                                   @RequestHeader(value = "Referer", required = false) String referer) {
        model.addAttribute(SEMESTER, semester);
        return referer != null ? "redirect:" + referer : "redirect:/";
    }

//    // Methode zum Abrufen des ausgewählten Mandanten für alle Templates
//    @ModelAttribute("selectedSemester")
//    public String getSelectedMandant(HttpSession session) {
//        String selectedSemester = (String) session.getAttribute(SEMESTER);
//        return selectedSemester != null ? selectedSemester : "Kein Semester ausgewählt";
//    }

//    // Methode zum Abrufen der dynamischen Liste der Mandanten für alle Seiten
//    @ModelAttribute("semesterList")
//    public List<String> getSemesters() {
//        return semesterService.findAll();
//    }
}

