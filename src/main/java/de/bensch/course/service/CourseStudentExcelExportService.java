package de.bensch.course.service;

import de.bensch.course.model.entity.StudentCourseSelection;
import de.bensch.course.service.poi.export.POIContext;
import de.bensch.course.service.poi.export.POIWeekDay;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

@Service
public class CourseStudentExcelExportService {

    public byte[] export(Collection<StudentCourseSelection> studentCourseSelection) throws IOException {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             var workbook = new XSSFWorkbook()) {

            Collection<POIWeekDay> poiWeekDays = POIWeekDay.parseWeekDays(studentCourseSelection);
            POIContext poiContext = new POIContext(workbook);
            poiWeekDays.forEach(e -> e.toExcel(poiContext));
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }


}
