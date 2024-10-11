package de.bensch.course.service.export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class POIUtil {
    private POIUtil() {

    }

    public static Row getRow(Sheet sheet, int index) {
        Row row = sheet.getRow(index);
        return row == null ? sheet.createRow(index) : row;
    }

    public static Cell getCell(Row row, int index) {
        Cell cell = row.getCell(index);
        return cell == null ? row.createCell(index) : cell;

    }

    public static Cell getCell(Sheet sheet, int rowIndex, int columnIndex) {
        Row row = getRow(sheet, rowIndex);
        return getCell(row, columnIndex);
    }

    public static void createMergedCell(Sheet sheet, int rowIndex, int columnIndex, CellStyle style, String value) {
        createMergedCell(sheet, rowIndex, columnIndex, style, value, (cr, sh) -> {
        });
    }

    public static void createMergedCell(Sheet sheet, int rowIndex, int columnIndex, CellStyle style, String value, BorderConfigurer borderConfigurer) {
        CellRangeAddress cellRange = new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex + 5);
        Cell cell = POIUtil.getCell(sheet, rowIndex, columnIndex);
        cell.setCellStyle(style);
        cell.setCellValue(value);
        sheet.addMergedRegion(cellRange);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, cellRange, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, cellRange, sheet);
        borderConfigurer.configureBorders(cellRange, sheet);

    }

    public static void getCell(XSSFSheet sheet, int rowIndex, int columnIndex, CellStyle borderLeft, String value) {
        Cell cell = getCell(sheet, rowIndex, columnIndex);
        cell.setCellStyle(borderLeft);
        cell.setCellValue(value);
    }
}

