package de.bensch.course.service.poi.export;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A factory class for creating and managing cell styles for Apache POI Excel workbooks.
 * This class caches styles to improve performance by reusing previously created styles.
 */
public class StyleFactory {

    private final XSSFWorkbook workbook;

    private final Map<StyleEnum, CellStyle> styles = new HashMap<>();

    /**
     * Constructs a StyleFactory with the specified workbook.
     *
     * @param workbook The workbook for which to create styles.
     */
    public StyleFactory(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    /**
     * Retrieves a cell style corresponding to the specified style enum.
     * If the style does not exist, it creates and caches it for future use.
     *
     * @param styleEnum The enum value representing the desired cell style.
     * @return The cell style associated with the specified enum.
     */
    public CellStyle getCellStyle(StyleEnum styleEnum) {
        return styles.computeIfAbsent(styleEnum, s -> styleEnum.getStyleSupplier().apply(workbook));
    }

    /**
     * Creates a cell style with specified border styles.
     *
     * @param workbook The workbook to which the cell style belongs.
     * @param right    The border style for the right side.
     * @param bottom   The border style for the bottom side.
     * @return A CellStyle object with the specified border styles.
     */
    private static CellStyle createBorderStyle(XSSFWorkbook workbook, BorderStyle right, BorderStyle bottom) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderRight(right);
        cellStyle.setBorderBottom(bottom);
        return cellStyle;
    }

    /**
     * Creates a font with specified attributes.
     *
     * @param workbook  The workbook to which the font belongs.
     * @param bold      Indicates whether the font should be bold.
     * @param size      The font size in points.
     * @param underline The underline style of the font.
     * @return A Font object with the specified attributes.
     */
    private static Font createFont(XSSFWorkbook workbook, boolean bold, short size, byte underline) {
        Font font = workbook.createFont();
        font.setBold(bold);
        font.setFontHeightInPoints(size);
        font.setUnderline(underline);
        return font;
    }

    /**
     * Enum representing different cell style configurations.
     */
    public enum StyleEnum {
        /**
         * Style for the header of a sheet.
         */
        SheetHeader(workbook -> {
            Font font = createFont(workbook, true, (short) 20, Font.U_SINGLE);
            CellStyle style = workbook.createCellStyle();
            style.setFont(font);
            return style;
        }),

        /**
         * Style for regular entry cells.
         */
        Entry(workbook -> {
            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            return style;
        }),

        /**
         * Style for entry cells with a medium bottom border.
         */
        EntryBorderBottom(workbook -> {
            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.MEDIUM);
            return style;
        }),

        /**
         * Style for entry cells with medium right and thin bottom borders.
         */
        EntryBorderRight(workbook -> createBorderStyle(workbook, BorderStyle.MEDIUM, BorderStyle.THIN)),

        /**
         * Style for entry cells with medium left and thin borders.
         */
        EntryBorderLeft(workbook -> {
            CellStyle style = createBorderStyle(workbook, BorderStyle.THIN, BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.MEDIUM);
            return style;
        }),

        /**
         * Style for entry cells with medium left and bottom borders.
         */
        EntryBorderLeftBottom(workbook -> {
            CellStyle style = createBorderStyle(workbook, BorderStyle.THIN, BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderBottom(BorderStyle.MEDIUM);
            return style;
        }),

        /**
         * Style for entry cells with medium right and bottom borders.
         */
        EntryBorderRightBottom(workbook -> {
            CellStyle style = createBorderStyle(workbook, BorderStyle.THIN, BorderStyle.THIN);
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setBorderBottom(BorderStyle.MEDIUM);
            return style;
        }),

        /**
         * Style for course headers with light green background and centered text.
         */
        CourseHeader(workbook -> {
            Font font = createFont(workbook, true, (short) 12, Font.U_NONE);
            CellStyle style = workbook.createCellStyle();
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            return style;
        });

        private final Function<XSSFWorkbook, CellStyle> cellStyleSupplier;

        StyleEnum(Function<XSSFWorkbook, CellStyle> cellStyleSupplier) {
            this.cellStyleSupplier = cellStyleSupplier;
        }

        /**
         * Retrieves the style supplier function for this enum.
         *
         * @return The function that creates the cell style.
         */
        public Function<XSSFWorkbook, CellStyle> getStyleSupplier() {
            return cellStyleSupplier;
        }
    }
}
