package de.bensch.course.service.poi.studentimport;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Utility class for handling common operations related to Excel cells using Apache POI.
 * This class contains static methods to extract string or boolean values from cells.
 * <p>
 * The class is designed to prevent instantiation by providing a private constructor.
 */
@Slf4j
public class CellUtil {


    private CellUtil() {
    }

    /**
     * Retrieves the value of a cell as a String.
     * <p>
     * This method handles the following cell types:
     * <ul>
     *   <li>If the cell is {@code null}, it returns an empty string.</li>
     *   <li>If the cell is of type {@code BLANK} or {@code STRING}, it returns the string value of the cell.</li>
     *   <li>If the cell is of type {@code NUMERIC}, it rounds the numeric value and returns it as a string.</li>
     *   <li>For other cell types, it logs a warning and returns an empty string.</li>
     * </ul>
     *
     * @param cell The Excel cell (of type {@code org.apache.poi.ss.usermodel.Cell}) to evaluate.
     * @return The string value of the cell, or an empty string if the cell is {@code null}, or of an unsupported type.
     */
    public static String getStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case BLANK, STRING -> cell.getStringCellValue();
            case NUMERIC -> "" + Math.round(cell.getNumericCellValue());
            default -> {
                log.warn("Not supported cell type: " + cell.getCellType());
                yield "";
            }
        };
    }

    /**
     * Retrieves the boolean value of a cell by comparing its string representation to "x".
     * <p>
     * This method first converts the cell's value to a string using the {@code getStringValue} method.
     * It then checks if the string value of the cell equals "x" (case-insensitive).
     * If the value equals "x" (ignoring case), it returns {@code true}; otherwise, it returns {@code false}.
     *
     * @param cell The Excel cell to evaluate.
     * @return {@code true} if the cell contains the string "x" (case-insensitive), otherwise {@code false}.
     */
    public static boolean getBooleanValue(Cell cell) {
        var value = CellUtil.getStringValue(cell);
        return "x".equalsIgnoreCase(value);
    }
}
