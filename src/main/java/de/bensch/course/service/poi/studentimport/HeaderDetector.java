package de.bensch.course.service.poi.studentimport;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;
import java.util.Optional;

/**
 * A utility class responsible for detecting and extracting headers from an Excel sheet.
 */
public class HeaderDetector {

    /**
     * Detects the header from the given Excel sheet by scanning through its rows.
     *
     * @param wahlzetteleingang the {@link Sheet} object representing the Excel sheet, can be null.
     * @return an {@link Optional} containing the {@link Header} if detected, otherwise an empty {@link Optional}.
     */
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

    /**
     * Extracts a {@link Header} from the given row if it contains valid columns.
     *
     * @param row the {@link Row} object representing the current row being examined.
     * @return an {@link Optional} containing the {@link Header} if the row contains valid columns, otherwise empty.
     */
    private Optional<Header> extractHeader(Row row) {
        Iterator<Cell> cellIterator = row.cellIterator();
        Header header = new Header(row.getRowNum());

        while (cellIterator.hasNext()) {
            var cell = cellIterator.next();
            // Try to find the corresponding column enum by matching the cell content
            Optional<Colum> column = Colum.findColumnNameIgnoreCase(CellUtil.getStringValue(cell));
            column.ifPresent(colum -> header.addColumn(colum, cell.getColumnIndex()));
        }

        // Return the header if it's not empty, otherwise return empty Optional
        if (header.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(header);
    }
}
