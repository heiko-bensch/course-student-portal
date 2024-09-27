package de.bensch.course.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;
import java.util.Optional;

public class HeaderDetector {

    @Nonnull
    public Optional<Header> detectHeader(@Nullable Sheet wahlzetteleingang) {
        if (wahlzetteleingang == null) {
            return Optional.empty();
        }
        for (Row row : wahlzetteleingang) {
            Optional<Header> header = extractHeader(row);
            if (header.isPresent()) {
                return header;
            }
        }
        return Optional.empty();
    }

    private Optional<Header> extractHeader(Row row) {
        Iterator<Cell> cellIterator = row.cellIterator();
        Header header = new Header(row.getRowNum());
        while (cellIterator.hasNext()) {
            var cell = cellIterator.next();
            Optional<Colum> column = Colum.findColumnNameIgnoreCase(CellUtil.getStringValue(cell));
            column.ifPresent(colum -> header.addColumn(colum, cell.getColumnIndex()));
        }
        if (header.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(header);
    }
}
