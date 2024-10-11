package de.bensch.course.service.export;

import lombok.Getter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class POIContext {
    @Getter
    private final XSSFWorkbook workbook;

    @Getter
    private final StyleFactory styleFactory;

    public POIContext(XSSFWorkbook workbook) {
        this.workbook = workbook;
        this.styleFactory = new StyleFactory(workbook);
    }
}
