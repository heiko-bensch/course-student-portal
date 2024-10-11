package de.bensch.course.service.poi.export;

import de.bensch.course.model.entity.Course;
import de.bensch.course.model.entity.StudentCourseSelection;
import lombok.Getter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static de.bensch.course.service.poi.export.StyleFactory.StyleEnum.*;
import static java.util.stream.Collectors.groupingBy;

/**
 * Represents a course for exporting to an Excel sheet, including associated student selections.
 * This class handles the organization and formatting of course information and student entries in an Excel sheet.
 */
public class POICourse {

    private final List<StudentCourseSelection> studentList;

    private final Course course;

    /**
     * Constructs a POICourse instance with the specified course and associated student course selections.
     *
     * @param course           The course associated with this POICourse instance.
     * @param courseSelections The list of student course selections for this course.
     */
    public POICourse(Course course, List<StudentCourseSelection> courseSelections) {
        this.course = course;
        this.studentList = courseSelections;
    }

    /**
     * Calculates the number of rows needed to represent the course, including student entries and additional spacing.
     *
     * @return The total number of rows required for this course in the Excel sheet.
     */
    public int getNofRows() {
        return studentList.size() + 3; // Add 3 for headers and spacing
    }

    /**
     * Writes the course and its student selections to the specified Excel sheet.
     *
     * @param context      The POIContext containing the workbook and style factory.
     * @param rowOffset    The starting row index for writing the course data.
     * @param columnOffset The starting column index for writing the course data.
     */
    public void toExcel(POIContext context, int rowOffset, int columnOffset) {
        XSSFSheet sheet = context.getWorkbook().getSheet(course.getDayOfWeek().getDisplayName());
        setColumnWith(sheet, columnOffset);
        var rowIndex = writeCourseHeader(context, sheet, rowOffset, columnOffset);
        rowIndex++;
        writeCourseEntries(context, sheet, rowIndex, columnOffset);
    }

    /**
     * Writes the student entries for the course to the specified sheet.
     *
     * @param context  The POIContext containing the workbook and style factory.
     * @param sheet    The sheet where student entries will be written.
     * @param rowIndex The starting row index for writing student entries.
     * @param column   The column index for writing student entries.
     */
    private void writeCourseEntries(POIContext context, XSSFSheet sheet, int rowIndex, int column) {
        List<StudentCourseSelection> sortedList = studentList.stream()
                .sorted(Comparator.comparing(StudentCourseSelection::getPriority)
                        .thenComparing(s -> s.getStudent().getGradeLevel())
                        .thenComparing(s -> s.getStudent().getClassName())
                        .thenComparing(s -> s.getStudent().getLastName()))
                .toList();

        for (int i = 0; i < sortedList.size(); i++) {
            int localRowIndex = rowIndex + i;
            writeCourseEntry(context, sheet, sortedList.get(i), localRowIndex, column, i == sortedList.size() - 1);
        }
    }

    /**
     * Writes a single student's selection entry to the specified sheet.
     *
     * @param context      The POIContext containing the workbook and style factory.
     * @param sheet        The sheet where the entry will be written.
     * @param selection    The student course selection to be written.
     * @param rowIndex     The row index where the entry will be written.
     * @param columnOffset The column index offset for the entry.
     * @param isLastEntry  A boolean indicating if this is the last entry for styling purposes.
     */
    private void writeCourseEntry(POIContext context, XSSFSheet sheet, StudentCourseSelection selection,
                                  int rowIndex, int columnOffset, boolean isLastEntry) {
        CellStyle borderLeft = context.getStyleFactory()
                .getCellStyle(isLastEntry ? EntryBorderLeftBottom : EntryBorderLeft);
        CellStyle entry = context.getStyleFactory().getCellStyle(isLastEntry ? EntryBorderBottom : Entry);
        CellStyle borderRight = context.getStyleFactory()
                .getCellStyle(isLastEntry ? EntryBorderRightBottom : EntryBorderRight);

        // Writing the data into the corresponding cells
        POIUtil.addCell(sheet, rowIndex, StudentColumn.Prio.getColumnIndex(columnOffset), borderLeft, selection.getPriority() + ". Wahl");
        POIUtil.addCell(sheet, rowIndex, StudentColumn.LastName.getColumnIndex(columnOffset), entry, selection.getStudent()
                .getLastName());
        POIUtil.addCell(sheet, rowIndex, StudentColumn.FirstName.getColumnIndex(columnOffset), entry, selection.getStudent()
                .getFirstName());
        POIUtil.addCell(sheet, rowIndex, StudentColumn.GradeLevel.getColumnIndex(columnOffset), entry, selection.getStudent()
                .getGradeLevel());
        POIUtil.addCell(sheet, rowIndex, StudentColumn.ClassName.getColumnIndex(columnOffset), entry, selection.getStudent()
                .getClassName());
        POIUtil.addCell(sheet, rowIndex, StudentColumn.Comment.getColumnIndex(columnOffset), borderRight, selection.getComment());
    }

    /**
     * Sets the column widths for the specified sheet based on the student columns defined.
     *
     * @param sheet        The sheet where column widths will be set.
     * @param columnOffset The column index offset for width settings.
     */
    private void setColumnWith(Sheet sheet, int columnOffset) {
        Stream.of(StudentColumn.values())
                .forEach(e -> sheet.setColumnWidth(e.getColumnIndex(columnOffset), e.getColumnSize()));
    }

    /**
     * Writes the header for the course in the specified sheet.
     *
     * @param context     The POIContext containing the workbook and style factory.
     * @param sheet       The sheet where the header will be written.
     * @param rowIndex    The starting row index for writing the header.
     * @param columnIndex The starting column index for writing the header.
     * @return The next row index after writing the header.
     */
    private int writeCourseHeader(POIContext context, XSSFSheet sheet, int rowIndex, int columnIndex) {
        var currentRowIndex = rowIndex;
        CellStyle cellStyle = context.getStyleFactory().getCellStyle(CourseHeader);

        // Merging cells for the course header and writing the course details
        POIUtil.createMergedCell(sheet, ++currentRowIndex, columnIndex, cellStyle, course.getName(),
                (cR, s) -> RegionUtil.setBorderTop(BorderStyle.MEDIUM, cR, s));
        POIUtil.createMergedCell(sheet, ++currentRowIndex, columnIndex, cellStyle, course.getGradeLevels());
        POIUtil.createMergedCell(sheet, ++currentRowIndex, columnIndex, cellStyle, course.getInstructor(),
                (cR, s) -> RegionUtil.setBorderBottom(BorderStyle.MEDIUM, cR, s));

        return currentRowIndex;
    }

    /**
     * Creates a list of POICourse instances from a list of student course selections.
     *
     * @param selectionList The list of student course selections to be converted into POICourse instances.
     * @return A list of POICourse instances.
     */
    static List<POICourse> createPOICourse(List<StudentCourseSelection> selectionList) {
        return selectionList.stream()
                .collect(groupingBy(StudentCourseSelection::getCourse))
                .entrySet()
                .stream()
                .map(e -> new POICourse(e.getKey(), e.getValue()))
                .toList();
    }

    /**
     * Enumeration representing the columns in the student course selection table.
     */
    @Getter
    enum StudentColumn {
        Prio(0, 7),
        LastName(1, 20),
        FirstName(2, 20),
        GradeLevel(3, 4),
        ClassName(4, 7),
        Comment(5, 20);

        private final int columnIndex;

        private final int columnSize;

        StudentColumn(int columnIndex, int columnSize) {
            this.columnIndex = columnIndex;
            this.columnSize = 256 * columnSize; // Size in units of 1/256th of a character width
        }

        /**
         * Gets the adjusted column index based on the provided offset.
         *
         * @param offset The offset to be added to the column index.
         * @return The adjusted column index.
         */
        public int getColumnIndex(int offset) {
            return columnIndex + offset;
        }
    }

}
