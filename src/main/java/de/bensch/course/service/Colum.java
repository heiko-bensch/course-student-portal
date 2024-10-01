package de.bensch.course.service;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enum representing the different columns used in the system.
 * Each enum constant is associated with a column name.
 */
public enum Colum {
    /**
     * Represents the last name column ("Name").
     */
    LastName("Name"),

    /**
     * Represents the first name column ("Vorname").
     */
    FirstName("Vorname"),

    /**
     * Represents the grade level column ("Klassenstufe").
     */
    GradeLevel("Klassenstufe"),

    /**
     * Represents the class column ("Klasse").
     */
    Class("Klasse"),

    /**
     * Represents the ballot submission status column ("Wahlzettel abgegeben").
     */
    BallotSubmitted("Wahlzettel abgegeben");

    /**
     * The name of the column associated with the enum constant.
     */
    @Getter
    private final String columnName;

    /**
     * Constructor to assign a column name to the enum constant.
     *
     * @param columnName the name of the column.
     */
    Colum(String columnName) {
        this.columnName = columnName;
    }

    /**
     * Finds a {@link Colum} enum constant by the provided column name, ignoring case.
     *
     * @param columnName the column name to search for.
     * @return an {@link Optional} containing the matching {@link Colum}, or empty if none matches.
     */
    public static Optional<Colum> findColumnNameIgnoreCase(String columnName) {
        return Arrays.stream(Colum.values())
                .filter(c -> c.columnName.equalsIgnoreCase(columnName))
                .findFirst();
    }
}

