package de.bensch.course.service;

import de.bensch.course.service.poi.studentimport.Colum;
import de.bensch.course.service.poi.studentimport.Header;
import de.bensch.course.service.poi.studentimport.HeaderDetector;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class HeaderDetectorTest {

    HeaderDetector headerDetector = new HeaderDetector();

    @Test
    void shouldReturnEmptyOptionalWhenSheetIsEmpty() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Students");
            Optional<Header> header = headerDetector.detectHeader(sheet);
            assertThat(header).isEmpty();
        }
    }

    @Test
    void shouldReturnCompleteHeaderForValidInput() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Students");
            Row row = sheet.createRow(3);

            row.createCell(3, CellType.NUMERIC).setCellValue(12);
            row.createCell(2, CellType.STRING).setBlank();
            row.createCell(4, CellType.STRING).setCellValue(Colum.FIRST_NAME.getColumnName());
            row.createCell(5, CellType.NUMERIC).setCellValue(12);
            row.createCell(7, CellType.STRING).setCellValue(Colum.LAST_NAME.getColumnName());
            row.createCell(10, CellType.STRING).setCellValue(Colum.GRADE_LEVEL.getColumnName());

            Optional<Header> header = headerDetector.detectHeader(sheet);
            assertThat(header).isNotEmpty();
            assertAll("header", () -> assertThat(header.get().isEmpty()).isFalse(),
                    () -> {
                        var firstName = header.get().getIndex(Colum.FIRST_NAME);
                        assertThat(firstName).isNotEmpty();
                        assertThat(firstName).contains(4);
                    },
                    () -> {

                        var lastName = header.get().getIndex(Colum.LAST_NAME);
                        assertThat(lastName).isNotEmpty();
                        assertThat(lastName).contains(7);
                    },
                    () -> {
                        var gradeLevel = header.get().getIndex(Colum.GRADE_LEVEL);
                        assertThat(gradeLevel).isNotEmpty();
                        assertThat(gradeLevel).contains(10);
                        assertThat(header.get().getIndex(Colum.CLASS)).isEmpty();
                    }
            );

        }
    }

}
