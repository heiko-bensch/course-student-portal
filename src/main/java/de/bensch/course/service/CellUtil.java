package de.bensch.course.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

@Slf4j
public class CellUtil {


    private CellUtil() {
    }

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

    public static boolean getBooleanValue(Cell cell) {
        var value = CellUtil.getStringValue(cell);
        return "x".equalsIgnoreCase(value);
    }
}
