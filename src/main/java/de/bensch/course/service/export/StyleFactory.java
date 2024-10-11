package de.bensch.course.service.export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StyleFactory {

    private final XSSFWorkbook workbook;

    private final Map<StyleEnum, CellStyle> styles = new HashMap<>();

    public StyleFactory(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public CellStyle getCellStyle(StyleEnum styleEnum) {
        return styles.computeIfAbsent(styleEnum, s -> styleEnum.getStyleSupplier().apply(workbook));
    }

    private static CellStyle createBorderStyle(XSSFWorkbook workbook, BorderStyle right, BorderStyle bottom) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderRight(right);
        cellStyle.setBorderBottom(bottom);
        return cellStyle;
    }

    private static Font createFont(XSSFWorkbook workbook, boolean bold, short size, byte underline) {
        Font font = workbook.createFont();
        font.setBold(bold);
        font.setFontHeightInPoints(size);
        font.setUnderline(underline);
        return font;
    }

    public enum StyleEnum {
        SheetHeader(workbook -> {
            Font font = createFont(workbook, true, (short) 20, Font.U_SINGLE);
            CellStyle style = workbook.createCellStyle();
            style.setFont(font);
            return style;
        }),
        Entry(workbook -> {
            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            return style;
        }),
        EntryBorderBottom(workbook -> {
            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.MEDIUM);
            return style;
        }),
        EntryBorderRight(workbook -> createBorderStyle(workbook, BorderStyle.MEDIUM, BorderStyle.THIN)),
        EntryBorderLeft(workbook -> {
            CellStyle style = createBorderStyle(workbook, BorderStyle.THIN, BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.MEDIUM);
            return style;
        }),
        EntryBorderLeftBottom(workbook -> {
            CellStyle style = createBorderStyle(workbook, BorderStyle.THIN, BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderBottom(BorderStyle.MEDIUM);
            return style;
        }),
        EntryBorderRightBottom(workbook -> {
            CellStyle style = createBorderStyle(workbook, BorderStyle.THIN, BorderStyle.THIN);
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setBorderBottom(BorderStyle.MEDIUM);
            return style;
        }),
        CourseHeader(workbook -> {
            Font font = createFont(workbook, true, (short) 12, Font.U_NONE);
            CellStyle style = workbook.createCellStyle();
            style.setFont(font);
            style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            return style;
        });
        // EntryBorderRightBottom(workbook -> createBorderStyle(workbook, BorderStyle.MEDIUM, BorderStyle.MEDIUM));

        private final Function<XSSFWorkbook, CellStyle> cellStyleSupplier;

        StyleEnum(Function<XSSFWorkbook, CellStyle> cellStyleSupplier) {
            this.cellStyleSupplier = cellStyleSupplier;
        }

        public Function<XSSFWorkbook, CellStyle> getStyleSupplier() {
            return cellStyleSupplier;
        }
    }
}

