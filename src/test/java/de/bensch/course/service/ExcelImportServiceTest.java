package de.bensch.course.service;

import de.bensch.course.model.Student;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ExcelImportServiceTest {

    private final ExcelImportService excelImportService = new ExcelImportService();


    @Test
    void shouldReadExcelContentAndParseExcelFileIntoStudentList1() throws ExcelImportException, IOException {
        byte[] bytes = Files.readAllBytes(Path.of("src", "test", "resources", "Excel_Import_File1.xlsx"));
        List<Student> expectedResult = List.of(
                new Student(null, "Name 1", "Nachname 1", "1", "Jupiter", true, new ArrayList<>()),
                new Student(null, "Name 2", "Nachname 2", "2", "Sonne", false, new ArrayList<>()),
                new Student(null, "Name 3", "Nachname 3", "3", "Venus", true, new ArrayList<>()),
                new Student(null, "Name 4", "Nachname 4", "4", "Erde", true, new ArrayList<>())
        );
        List<Student> read = excelImportService.readExcelContent(bytes);
        assertThat(read).isEqualTo(expectedResult);
    }


}
