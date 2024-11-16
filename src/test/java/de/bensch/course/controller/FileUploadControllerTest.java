package de.bensch.course.controller;


import de.bensch.course.config.constants.SessionConstants;
import de.bensch.course.service.ExcelImportService;
import de.bensch.course.service.SemesterService;
import de.bensch.course.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static de.bensch.course.controller.FileUploadController.MODEL_MESSAGE;
import static de.bensch.course.controller.routing.StudentPaths.URL_STUDENT_UPLOAD_FORM;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileUploadController.class)
@Import(TestSecurityConfig.class)
class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SemesterService semesterService;

    @MockBean
    private ExcelImportService excelImportService;

    @MockBean
    private StudentService studentService;

    @Test
    void testUploadStudents_SuccessfulUpload() throws Exception {
        // Simuliere eine Excel-Datei mit Inhalten
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "students.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "Sample Excel content".getBytes(StandardCharsets.UTF_8)  // Simuliere Dateiinhalte
        );

        mockMvc.perform(multipart(URL_STUDENT_UPLOAD_FORM)
                        .file(file)
                        .sessionAttr(SessionConstants.SEMESTER, "01/2024"))
                .andExpect(status().is2xxSuccessful());  // Erwartet eine Umleitung nach erfolgreichem Upload
    }

    @Test
    void testUploadStudents_EmptyFile() throws Exception {
        // Simuliere eine leere Datei
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "students.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new byte[0]  // Leerer Inhalt
        );

        mockMvc.perform(multipart(URL_STUDENT_UPLOAD_FORM)
                        .file(emptyFile)
                        .sessionAttr(SessionConstants.SEMESTER, "01/2024"))
                .andExpect(status().isOk())  // Erwartet Status 200, da auf die gleiche Seite zurückgekehrt wird
                .andExpect(model().attribute(MODEL_MESSAGE, "Please select a file to upload"));
    }

    @Test
    void testUploadStudents_FileProcessingError() throws Exception {
        // Simuliere eine Datei mit Inhalten, die einen Fehler verursachen
        MockMultipartFile fileWithError = new MockMultipartFile(
                "file",
                "students.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "Faulty content".getBytes(StandardCharsets.UTF_8)
        );

        // Simuliere einen Fehler im Service (du kannst das Mocken mit Mockito verwenden, wenn nötig)
        mockMvc.perform(multipart(URL_STUDENT_UPLOAD_FORM)
                        .file(fileWithError)
                        .sessionAttr(SessionConstants.SEMESTER, "01/2024"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(MODEL_MESSAGE))
                .andExpect(model().attribute(MODEL_MESSAGE, "No data found in the Excel spreadsheet."));  // Beispielmeldung
    }
}
