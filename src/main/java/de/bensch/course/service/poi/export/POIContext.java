package de.bensch.course.service.poi.export;

import lombok.Getter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Represents the context for creating and managing an Excel workbook.
 * This class encapsulates the workbook instance and its associated style factory.
 */
@Getter
public class POIContext {

    private final XSSFWorkbook workbook;

    private final StyleFactory styleFactory;

    /**
     * Constructs a POIContext with the specified workbook.
     * Initializes the style factory using the provided workbook instance.
     *
     * @param workbook The XSSFWorkbook instance that this context will manage.
     */
    public POIContext(XSSFWorkbook workbook) {
        this.workbook = workbook;
        this.styleFactory = new StyleFactory(workbook);
    }
}
