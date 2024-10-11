package de.bensch.course.service.export;

import de.bensch.course.model.entity.Course;
import de.bensch.course.model.entity.StudentCourseSelection;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Comparator;
import java.util.List;

import static de.bensch.course.service.export.StyleFactory.StyleEnum.*;
import static java.util.stream.Collectors.groupingBy;

public class POICourse {

    private final List<StudentCourseSelection> studentList;

    private final Course course;


    public POICourse(Course course, List<StudentCourseSelection> courseSelections) {
        this.course = course;
        this.studentList = courseSelections;


    }


    public int getNofRows() {
        return studentList.size() + 3;
    }


    public int getNofColumns() {
        return 5;
    }

    public void toExcel(POIContext context) {
        XSSFSheet sheet = context.getWorkbook().getSheet(course.getDayOfWeek().name());
        setColumnWith(sheet, 0);
        var rowIndex = writeCourseHeader(context, sheet, 4, 0);
        rowIndex++;
        writeCourseEntries(context, sheet, rowIndex, 0);

    }

    private void writeCourseEntries(POIContext context, XSSFSheet sheet, int rowIndex, int column) {
        int localRowIndex = rowIndex;
        List<StudentCourseSelection> sortedList = studentList.stream()
                .sorted(Comparator.comparing(StudentCourseSelection::getPriority)
                        .thenComparing(s -> s.getStudent().getGradeLevel())
                        .thenComparing(s -> s.getStudent().getClassName())
                        .thenComparing(s -> s.getStudent().getLastName()))
                .toList();
        for (int i = 0; i < sortedList.size(); i++) {
            localRowIndex = rowIndex + i;
            writeCourseEntry(context, sheet, sortedList.get(i), localRowIndex, column, i == sortedList.size() - 1);
        }
    }

    private void writeCourseEntry(POIContext context, XSSFSheet sheet, StudentCourseSelection s, int rowIndex, int columnIndex, boolean isLastEntry) {
        CellStyle borderLeft = context.getStyleFactory()
                .getCellStyle((isLastEntry) ? EntryBorderLeftBottom : EntryBorderLeft);
        CellStyle entry = context.getStyleFactory().getCellStyle((isLastEntry) ? EntryBorderBottom : Entry);

        CellStyle borderRight = context.getStyleFactory()
                .getCellStyle(isLastEntry ? EntryBorderRightBottom : EntryBorderRight);
        var currentColumnIndex = columnIndex;

        POIUtil.getCell(sheet, rowIndex, currentColumnIndex++, borderLeft, String.valueOf(s.getPriority()));
        POIUtil.getCell(sheet, rowIndex, currentColumnIndex++, entry, s.getStudent().getLastName());
        POIUtil.getCell(sheet, rowIndex, currentColumnIndex++, entry, s.getStudent().getFirstName());
        POIUtil.getCell(sheet, rowIndex, currentColumnIndex++, entry, s.getStudent().getGradeLevel());
        POIUtil.getCell(sheet, rowIndex, currentColumnIndex++, entry, s.getStudent().getClassName());
        POIUtil.getCell(sheet, rowIndex, currentColumnIndex++, borderRight, s.getComment());

    }

    private void setColumnWith(Sheet sheet, int columnIndex) {
        var currentColumnIndex = columnIndex;
        sheet.setColumnWidth(currentColumnIndex++, 4 * 256);
        sheet.setColumnWidth(currentColumnIndex++, 20 * 256);
        sheet.setColumnWidth(currentColumnIndex++, 20 * 256);
        sheet.setColumnWidth(currentColumnIndex++, 4 * 256);
        sheet.setColumnWidth(currentColumnIndex++, 20 * 256);
    }

    private int writeCourseHeader(POIContext context, XSSFSheet sheet, int rowIndex, int columnIndex) {
        var currentRowIndex = rowIndex + 4;

        CellStyle cellStyle = context.getStyleFactory().getCellStyle(StyleFactory.StyleEnum.CourseHeader);

        POIUtil.createMergedCell(sheet, ++currentRowIndex, columnIndex, cellStyle, course.getName(), (cR, s) -> RegionUtil.setBorderTop(BorderStyle.MEDIUM, cR, s));
        POIUtil.createMergedCell(sheet, ++currentRowIndex, columnIndex, cellStyle, course.getGradeLevels());
        POIUtil.createMergedCell(sheet, ++currentRowIndex, columnIndex, cellStyle, course.getInstructor(), (cR, s) -> RegionUtil.setBorderBottom(BorderStyle.MEDIUM, cR, s));

        return currentRowIndex;
    }

    static List<POICourse> createPOICourse(List<StudentCourseSelection> selectionList) {
        return selectionList
                .stream()
                .collect(groupingBy(StudentCourseSelection::getCourse))
                .entrySet().
                stream()
                .map(e -> new POICourse(e.getKey(), e.getValue())).toList();

    }

}
