package de.bensch.course.service.export;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelErstellen {
    public static void main(String[] args) {
        Workbook workbook = new XSSFWorkbook();

        // Erstellen von vier Arbeitsblättern
        for (int i = 0; i < 4; i++) {
            Sheet sheet = workbook.createSheet("Reiter " + (i + 1));
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("Inhalt für Reiter " + (i + 1));
        }

        // Speichern der Excel-Datei
        try (FileOutputStream outputStream = new FileOutputStream("ExcelDatei.xlsx")) {
            workbook.write(outputStream);
            System.out.println("Excel-Datei erfolgreich erstellt!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
