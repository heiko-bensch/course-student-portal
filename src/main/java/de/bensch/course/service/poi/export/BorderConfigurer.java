package de.bensch.course.service.poi.export;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

@FunctionalInterface
public interface BorderConfigurer {
    void configureBorders(CellRangeAddress cellRange, Sheet sheet);
}
