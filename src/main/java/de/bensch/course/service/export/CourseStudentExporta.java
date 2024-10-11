package de.bensch.course.service.export;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.entity.Course;
import de.bensch.course.model.entity.StudentCourseSelection;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class CourseStudentExporta {


    private Sheet sheet;

    private XSSFWorkbook workbook;

    private StyleFactory styleFactory;


    public byte[] export(Collection<StudentCourseSelection> studentCourseSelection) throws IOException {
        workbook = new XSSFWorkbook();
        styleFactory = new StyleFactory(workbook);
        Map<WeekDay, List<StudentCourseSelection>> dayListMap = studentCourseSelection.stream()
                .collect(groupingBy(StudentCourseSelection::getWeekDay));

        dayListMap.forEach(this::exportWeekDay);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void exportWeekDay(WeekDay weekDay, List<StudentCourseSelection> courseSelectionList) {
        sheet = workbook.createSheet(weekDay.name()); //ToDo in deutshch

        int rowIndex = writeSheetHeader(weekDay);


        Map<Course, List<StudentCourseSelection>> courseListMap = courseSelectionList.stream()
                .collect(groupingBy(StudentCourseSelection::getCourse));
        courseListMap.forEach((key, value) -> exportCourse(key, value, rowIndex, 0));

    }

    private void exportCourse(Course key, List<StudentCourseSelection> value, int rowIndex, int columnIndex) {

        var tmpRowIndex = writeCourseHeader(rowIndex, columnIndex, key);

        Collection<StudentCourseSelection> list = value.stream()
                .sorted(Comparator.comparing(StudentCourseSelection::getPriority)
                        .thenComparing(s -> s.getStudent().getGradeLevel())
                        .thenComparing(s -> s.getStudent().getClassName())
                        .thenComparing(s -> s.getStudent().getLastName()))
                .toList();

        for (StudentCourseSelection selection : list) {
            tmpRowIndex++;
            writeCourseSelection(selection, tmpRowIndex, columnIndex);
        }


    }

    private void writeCourseSelection(StudentCourseSelection s, int rowIndex, int i) {
        CellStyle borderLeft = styleFactory.getCellStyle(StyleFactory.StyleEnum.EntryBorderLeft);
        CellStyle entry = styleFactory.getCellStyle(StyleFactory.StyleEnum.Entry);

        CellStyle borderRight = styleFactory.getCellStyle(StyleFactory.StyleEnum.EntryBorderRight);

        var cell = POIUtil.getCell(sheet, rowIndex, i++);
        cell.setCellStyle(borderLeft);
        cell.setCellValue(s.getPriority());

        cell = POIUtil.getCell(sheet, rowIndex, i++);
        cell.setCellStyle(entry);
        cell.setCellValue(s.getStudent().getLastName());

        cell = POIUtil.getCell(sheet, rowIndex, i++);
        cell.setCellStyle(entry);
        cell.setCellValue(s.getStudent().getFirstName());

        cell = POIUtil.getCell(sheet, rowIndex, i++);
        cell.setCellStyle(entry);
        cell.setCellValue(s.getStudent().getGradeLevel());

        cell = POIUtil.getCell(sheet, rowIndex, i++);
        cell.setCellValue(s.getStudent().getClassName());
        cell.setCellStyle(entry);

        cell = POIUtil.getCell(sheet, rowIndex, i++);
        cell.setCellStyle(borderRight);
        cell.setCellValue(s.getComment());
    }

    private int writeSheetHeader(WeekDay weekDay) {
        int rowIndex = 0;
        int columnIndex = 3;
        Row row = POIUtil.getRow(sheet, rowIndex);
        Cell cell;
        CellStyle cellStyle = styleFactory.getCellStyle(StyleFactory.StyleEnum.SheetHeader);

        cell = POIUtil.getCell(row, columnIndex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Kurswahl P1");

        rowIndex++;
        row = POIUtil.getRow(sheet, rowIndex);
        cell = POIUtil.getCell(row, columnIndex);
        cell.setCellValue(weekDay.name());
        cell.setCellStyle(cellStyle);

        return rowIndex;
    }

    private int writeCourseHeader(int rowIndex, int columnIndex, Course key) {
        var tmpRowIndex = rowIndex + 4;
        sheet.setColumnWidth(0, 4 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 4 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        CellStyle cellStyle = styleFactory.getCellStyle(StyleFactory.StyleEnum.CourseHeader);

//        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
//        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        cellStyle.setAlignment(HorizontalAlignment.CENTER);


        Cell cell = POIUtil.getCell(sheet, ++tmpRowIndex, columnIndex);
        CellRangeAddress cellRange = new CellRangeAddress(tmpRowIndex, tmpRowIndex, columnIndex, columnIndex + 5);
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegion(cellRange);
        cell.setCellValue(key.getName());
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, cellRange, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, cellRange, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, cellRange, sheet);


        cell = POIUtil.getCell(sheet, ++tmpRowIndex, columnIndex);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(key.getGradeLevels());
        cellRange = new CellRangeAddress(tmpRowIndex, tmpRowIndex, columnIndex, columnIndex + 5);
        sheet.addMergedRegion(cellRange);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, cellRange, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, cellRange, sheet);


        cell = POIUtil.getCell(sheet, ++tmpRowIndex, columnIndex);
        cellRange = new CellRangeAddress(tmpRowIndex, tmpRowIndex, columnIndex, columnIndex + 5);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(key.getInstructor());
        sheet.addMergedRegion(cellRange);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, cellRange, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, cellRange, sheet);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, cellRange, sheet);


        return tmpRowIndex;
    }


}
