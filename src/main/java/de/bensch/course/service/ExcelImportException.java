package de.bensch.course.service;

public class ExcelImportException extends Exception {
    public ExcelImportException(String message, Exception e) {
        super(message, e);
    }
}
