package de.bensch.course.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents the header of an Excel sheet, storing information about the row number
 * and the mapping of columns to their respective indices.
 */
public class Header {

    /**
     * The row number of the header in the Excel sheet.
     */
    private final int rowNum;

    /**
     * A map containing the mapping of {@link Colum} enum constants to their column indices.
     */
    private final Map<Colum, Integer> columns;

    /**
     * Constructor for creating a Header instance with a specific row number.
     *
     * @param rowNum the row number where the header is located in the Excel sheet.
     */
    public Header(int rowNum) {
        this.rowNum = rowNum;
        this.columns = new HashMap<>();
    }

    /**
     * Adds a column to the header with its corresponding index.
     *
     * @param colum       the {@link Colum} enum constant representing the column.
     * @param columnIndex the index of the column in the Excel sheet.
     */
    public void addColumn(Colum colum, int columnIndex) {
        this.columns.put(colum, columnIndex);
    }

    /**
     * Checks if the header contains any columns.
     *
     * @return {@code true} if the header has no columns, otherwise {@code false}.
     */
    public boolean isEmpty() {
        return columns.isEmpty();
    }

    /**
     * Retrieves the index of the specified column, if present.
     *
     * @param colum the {@link Colum} enum constant representing the column.
     * @return an {@link Optional} containing the column index, or empty if the column is not found.
     */
    public Optional<Integer> getIndex(Colum colum) {
        return Optional.ofNullable(columns.get(colum));
    }

    /**
     * Returns the row number where the header is located in the Excel sheet.
     *
     * @return the row number of the header.
     */
    public int getHeaderRowNum() {
        return rowNum;
    }
}
