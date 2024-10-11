package de.bensch.course.service.poi.export;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.entity.StudentCourseSelection;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

/**
 * Represents a collection of courses organized by the day of the week.
 * This class is responsible for processing course selections and exporting them to an Excel sheet.
 */
public class POIWeekDay {

    private final WeekDay weekDay;

    private static final int ROW_GAP = 3;

    private static final int START_COLUMN_OFFSET = 1;

    private final List<POICourse> list1 = new ArrayList<>();

    private final List<POICourse> list2 = new ArrayList<>();

    private final List<POICourse> list3 = new ArrayList<>();

    /**
     * Constructs a POIWeekDay object with the specified weekday and list of courses.
     * The courses are distributed across three lists based on their index.
     *
     * @param weekDay The weekday for which courses are organized.
     * @param value   The list of POICourse objects to be distributed.
     */
    public POIWeekDay(WeekDay weekDay, List<POICourse> value) {
        this.weekDay = weekDay;
        for (int i = 0; i < value.size(); i++) {
            var currentElement = value.get(i);

            switch (i % 3) {
                case 0 -> list1.add(currentElement);
                case 1 -> list2.add(currentElement);
                default -> list3.add(currentElement);
            }
        }
    }

    /**
     * Parses a collection of student course selections and organizes them by weekday.
     *
     * @param selectionList The collection of StudentCourseSelection objects to parse.
     * @return A list of POIWeekDay objects, each representing a day of the week with associated courses.
     */
    public static List<POIWeekDay> parseWeekDays(Collection<StudentCourseSelection> selectionList) {
        return selectionList.stream()
                .collect(groupingBy(StudentCourseSelection::getWeekDay))
                .entrySet()
                .stream()
                .map((e) -> new POIWeekDay(e.getKey(), POICourse.createPOICourse(e.getValue())))
                .sorted(Comparator.comparing(s -> s.weekDay))
                .toList();
    }

    /**
     * Exports the course information to an Excel sheet using the specified context.
     *
     * @param poiContext The context containing the Excel workbook and styles.
     */
    public void toExcel(POIContext poiContext) {
        writeSheetHeader(poiContext);
        writeCourses(poiContext);
    }

    /**
     * Writes the courses to the Excel sheet in their respective columns.
     *
     * @param poiContext The context containing the Excel workbook and styles.
     */
    private void writeCourses(POIContext poiContext) {
        writeCoursesColumn(poiContext, START_COLUMN_OFFSET, list1);
        writeCoursesColumn(poiContext, START_COLUMN_OFFSET + 7, list2);
        writeCoursesColumn(poiContext, START_COLUMN_OFFSET + 14, list3);
    }

    /**
     * Writes a specific list of courses to a column in the Excel sheet.
     *
     * @param poiContext  The context containing the Excel workbook and styles.
     * @param columOffSet The starting column offset for the courses.
     * @param courses     The list of courses to be written to the column.
     */
    private void writeCoursesColumn(POIContext poiContext, int columOffSet, List<POICourse> courses) {
        int currentRowOffset = ROW_GAP;
        for (POICourse course : courses) {
            course.toExcel(poiContext, currentRowOffset, columOffSet);
            currentRowOffset += course.getNofRows() + ROW_GAP;
        }
    }

    /**
     * Writes the header for the Excel sheet, including the course selection title and the weekday name.
     *
     * @param poiContext The context containing the Excel workbook and styles.
     */
    private void writeSheetHeader(POIContext poiContext) {
        int rowIndex = 0;
        int columnIndex = 3;
        XSSFSheet sheet = poiContext.getWorkbook().createSheet(weekDay.getDisplayName());
        CellStyle cellStyle = poiContext.getStyleFactory().getCellStyle(StyleFactory.StyleEnum.SheetHeader);

        POIUtil.addCell(sheet, rowIndex, columnIndex, cellStyle, "Kurswahl P1");
        POIUtil.addCell(sheet, ++rowIndex, columnIndex, cellStyle, weekDay.getDisplayName());
    }
}
