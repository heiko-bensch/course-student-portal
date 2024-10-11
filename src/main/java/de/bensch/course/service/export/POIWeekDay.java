package de.bensch.course.service.export;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.entity.StudentCourseSelection;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

public class POIWeekDay {


    private final WeekDay weekDay;

    private final List<POICourse> list;

    public POIWeekDay(WeekDay weekDay, List<POICourse> value) {
        this.weekDay = weekDay;

        this.list = value;
    }

    public static List<POIWeekDay> parseWeekDays(Collection<StudentCourseSelection> selectionList) {
        return selectionList.stream()
                .collect(groupingBy(StudentCourseSelection::getWeekDay))
                .entrySet()
                .stream()
                .map((e) -> new POIWeekDay(e.getKey(), POICourse.createPOICourse(e.getValue()))).toList();
    }


    public void toExcel(POIContext poiContext) {
        writeSheetHeader(poiContext);
        writeCourses(poiContext);

    }

    private void writeCourses(POIContext poiContext) {
        list.forEach(c -> c.toExcel(poiContext));
    }

    private int writeSheetHeader(POIContext poiContext) {
        int rowIndex = 0;
        int columnIndex = 3;
        XSSFSheet sheet = poiContext.getWorkbook().createSheet(weekDay.name());
        Row row = POIUtil.getRow(sheet, rowIndex);
        Cell cell;
        CellStyle cellStyle = poiContext.getStyleFactory().getCellStyle(StyleFactory.StyleEnum.SheetHeader);

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
}
