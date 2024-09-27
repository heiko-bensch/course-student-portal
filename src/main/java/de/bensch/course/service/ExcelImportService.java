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

@Service
@Slf4j
public class ExcelImportService {

    public List<Student> readExcelContent(byte[] content) throws IOException {
        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(content));
        Sheet wahlzetteleingang = workbook.getSheet("Wahlzetteleingang");
        HeaderDetector headerDetector = new HeaderDetector();
        Optional<Header> header = headerDetector.detectHeader(wahlzetteleingang);

        return header
                .map(h -> readStudents(wahlzetteleingang, h))
                .orElse(new ArrayList<>());
    }

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
