package de.bensch.course.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Header {
    private final int rowNum;

    private final Map<Colum, Integer> columns;

    public Header(int rowNum) {
        this.rowNum = rowNum;
        this.columns = new HashMap<>();
    }

    public void addColumn(Colum colum, int columnIndex) {
        this.columns.put(colum, columnIndex);

    }

    public boolean isEmpty() {
        return columns.isEmpty();
    }


    public Optional<Integer> getIndex(Colum colum) {
        return Optional.ofNullable(columns.get(colum));
    }

    public int getHeaderRowNum() {
        return rowNum;
    }
}
