package de.bensch.course.controller;

import static de.bensch.course.controller.routing.StudentMappings.STUDENT_LIST;
import static de.bensch.course.controller.routing.StudentMappings.STUDENT_UPLOAD_FORM;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import de.bensch.course.config.constants.SessionConstants;
import de.bensch.course.model.entity.Student;
import de.bensch.course.service.ExcelImportException;
import de.bensch.course.service.ExcelImportService;
import de.bensch.course.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@AllArgsConstructor
@SuppressWarnings("squid:S3753")
@SessionAttributes(SessionConstants.SEMESTER)
public class FileUploadController {

    private final ExcelImportService excelImportService;

    private final StudentService studentService;

    @PostMapping(STUDENT_UPLOAD_FORM)
    public String uploadStudents(Model model, @RequestParam("file") MultipartFile file) throws ExcelImportException {
        String message;
        String semester = (String) model.getAttribute(SessionConstants.SEMESTER);
        try {
            if (file.isEmpty()) {
                message = "Please select a file to upload";
            } else {
                List<Student> studentList = excelImportService.readExcelContent(semester, file.getBytes());
                if (studentList.isEmpty()) {
                    message = "No data found in the Excel spreadsheet.";
                } else {
                    studentService.saveAll(studentList);
                    return "redirect:" + STUDENT_LIST;
                }
            }
        } catch (Exception e) {
            log.error("Error uploading file.", e);
            message = "Could not upload the file. " + e.getMessage();

        }
        model.addAttribute("message", message);
        return STUDENT_LIST;
    }


}
