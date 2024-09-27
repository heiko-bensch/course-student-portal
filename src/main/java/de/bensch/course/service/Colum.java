package de.bensch.course.service;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum Colum {
    LastName("Name"),
    FirstName("Vorname"),
    GradeLevel("Klassenstufe"),
    Class("Klasse"),
    BallotSubmitted("Wahlzettel abgegeben");

    @Getter
    private final String columnName;

    Colum(String columnName) {
        this.columnName = columnName;
    }


    public static Optional<Colum> findColumnNameIgnoreCase(String columnName) {
        return Arrays.stream(Colum.values())
                .filter(c -> c.columnName.equalsIgnoreCase(columnName)).
                findFirst();
    }

}
