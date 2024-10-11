package de.bensch.course.service.poi.export;

import de.bensch.course.model.WeekDay;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * Utility class for performing common operations on Apache POI Excel sheets.
 * This class provides methods for handling rows, cells, merged cells, and styles.
 */
public class POIUtil {

    // Private constructor to prevent instantiation
    private POIUtil() {
    }

    /**
     * Retrieves a row from the specified sheet, creating it if it does not exist.
     *
     * @param sheet The sheet from which to retrieve the row.
     * @param index The index of the row to retrieve.
     * @return The existing row or a newly created row at the specified index.
     */
    public static Row getRow(Sheet sheet, int index) {
        Row row = sheet.getRow(index);
        return row == null ? sheet.createRow(index) : row;
    }

    /**
     * Adds a cell to a specified row at the given index, creating the cell if it does not exist.
     *
     * @param row   The row to which the cell will be added.
     * @param index The index of the cell to add.
     * @return The existing cell or a newly created cell at the specified index.
     */
    public static Cell addCell(Row row, int index) {
        Cell cell = row.getCell(index);
        return cell == null ? row.createCell(index) : cell;
    }

    /**
     * Adds a cell to the specified sheet at the given row and column indices, creating the row and cell if necessary.
     *
     * @param sheet       The sheet where the cell will be added.
     * @param rowIndex    The index of the row where the cell will be added.
     * @param columnIndex The index of the column where the cell will be added.
     * @return The existing cell or a newly created cell at the specified indices.
     */
    public static Cell addCell(Sheet sheet, int rowIndex, int columnIndex) {
        Row row = getRow(sheet, rowIndex);
        return addCell(row, columnIndex);
    }

    /**
     * Creates a merged cell in the specified sheet with the given style and value.
     * The merged cell spans from the specified column index to the next five columns.
     *
     * @param sheet       The sheet where the merged cell will be created.
     * @param rowIndex    The index of the row where the merged cell will be created.
     * @param columnIndex The index of the starting column where the merged cell will be created.
     * @param style       The style to apply to the merged cell.
     * @param value       The value to set for the merged cell.
     */
    public static void createMergedCell(Sheet sheet, int rowIndex, int columnIndex, CellStyle style, String value) {
        createMergedCell(sheet, rowIndex, columnIndex, style, value, (cr, sh) -> {
        });
    }

    /**
     * Creates a merged cell in the specified sheet with the given style, value, and border configuration.
     * The merged cell spans from the specified column index to the next five columns.
     *
     * @param sheet            The sheet where the merged cell will be created.
     * @param rowIndex         The index of the row where the merged cell will be created.
     * @param columnIndex      The index of the starting column where the merged cell will be created.
     * @param style            The style to apply to the merged cell.
     * @param value            The value to set for the merged cell.
     * @param borderConfigurer A functional interface to configure borders for the merged cell.
     */
    public static void createMergedCell(Sheet sheet, int rowIndex, int columnIndex, CellStyle style, String value, BorderConfigurer borderConfigurer) {
        CellRangeAddress cellRange = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex + 5);
        Cell cell = POIUtil.addCell(sheet, rowIndex, columnIndex);
        cell.setCellStyle(style);
        cell.setCellValue(value);
        sheet.addMergedRegion(cellRange);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, cellRange, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, cellRange, sheet);
        borderConfigurer.configureBorders(cellRange, sheet);
    }

    /**
     * Adds a cell to the specified sheet at the given row and column indices,
     * applying a specified border style and setting the cell's value.
     *
     * @param sheet       The sheet where the cell will be added.
     * @param rowIndex    The index of the row where the cell will be added.
     * @param columnIndex The index of the column where the cell will be added.
     * @param borderLeft  The style to apply to the left border of the cell.
     * @param value       The value to set for the cell.
     */
    public static void addCell(XSSFSheet sheet, int rowIndex, int columnIndex, CellStyle borderLeft, String value) {
        Cell cell = addCell(sheet, rowIndex, columnIndex);
        cell.setCellStyle(borderLeft);
        cell.setCellValue(value);
    }

    /**
     * Returns a color from the {@link IndexedColors} enum that corresponds to the given weekday.
     * Each weekday is mapped to a specific color.
     *
     * @param dayOfWeek the {@link WeekDay} representing the day of the week for which the color is needed
     * @return the {@link IndexedColors} value representing the color associated with the given weekday
     * or a default color if the weekday is not explicitly mapped
     */
    public static IndexedColors getColorForWeekday(WeekDay dayOfWeek) {
        return switch (dayOfWeek) {
            case Thursday -> IndexedColors.LIGHT_GREEN;
            case Monday -> IndexedColors.LIGHT_CORNFLOWER_BLUE;
            case Tuesday -> IndexedColors.LEMON_CHIFFON;
            case Wednesday -> IndexedColors.LIGHT_TURQUOISE;
        };
    }
}

