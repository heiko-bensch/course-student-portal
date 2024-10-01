package de.bensch.course.service;

import de.bensch.course.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for importing student data from an Excel sheet.
 */
@Service
@Slf4j
public class ExcelImportService {

    /**
     * Reads the content of an Excel file and returns a list of students.
     * The student data are on the sheet "Wahlzetteleingang"
     *
     * @param content the byte array representing the Excel file.
     * @return a list of {@link Student} objects parsed from the Excel file.
     * @throws ExcelImportException if an error occurs while reading the Excel file.
     */
    public List<Student> readExcelContent(byte[] content) throws ExcelImportException {
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(new ByteArrayInputStream(content));
            Sheet wahlzetteleingang = workbook.getSheet("Wahlzetteleingang");
            HeaderDetector headerDetector = new HeaderDetector();
            Optional<Header> header = headerDetector.detectHeader(wahlzetteleingang);

            return header
                    .map(h -> readStudents(wahlzetteleingang, h))
                    .orElse(new ArrayList<>());
        } catch (IOException e) {
            throw new ExcelImportException("Error importing an Excel sheet.\n" + e.getMessage(), e);
        }
    }

    /**
     * Reads and converts each row of the Excel sheet into a list of {@link Student} objects.
     *
     * @param sheet  the Excel {@link Sheet} object representing the data sheet.
     * @param header the {@link Header} object containing the detected column indices.
     * @return a list of students extracted from the Excel sheet.
     */
    private List<Student> readStudents(Sheet sheet, Header header) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        List<Student> studentList = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() > header.getHeaderRowNum()) {
                studentFromRow(header, row)
                        .ifPresent(studentList::add);
            }
        }
        return studentList;
    }


    /**
     * Extracts a {@link Student} object from a single row of the Excel sheet.
     * The method checks for the presence of each column and sets the corresponding student properties.
     *
     * @param header the {@link Header} object containing the detected column indices.
     * @param row    the {@link Row} object representing the current row of data.
     * @return an {@link Optional} containing the {@link Student}, or empty if the row does not contain valid student data.
     */
    private Optional<Student> studentFromRow(Header header, Row row) {

        Student student;
        student = new Student();

        header.getIndex(Colum.FirstName).ifPresent(c -> {
            var val = CellUtil.getStringValue(row.getCell(c));
            student.setFirstName(val);
        });

        header.getIndex(Colum.LastName).ifPresent(c -> {
            var val = CellUtil.getStringValue(row.getCell(c));
            student.setLastName(val);
        });

        header.getIndex(Colum.GradeLevel).ifPresent(c -> {
            var val = CellUtil.getStringValue(row.getCell(c));
            student.setGradeLevel(val);
        });

        header.getIndex(Colum.Class).ifPresent(c -> {
            var val = CellUtil.getStringValue(row.getCell(c));
            student.setClassName(val);
        });

        header.getIndex(Colum.BallotSubmitted).ifPresent(c -> {
            var val = CellUtil.getBooleanValue(row.getCell(c));
            student.setBallotSubmitted(val);
        });

        if (student.getFirstName().isBlank() && student.getLastName().isBlank()) {
            return Optional.empty();
        }
        return Optional.of(student);
    }

}
