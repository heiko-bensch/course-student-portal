package de.bensch.course.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Service for managing semester information.
 * This service provides functionalities for retrieving all possible semesters and determining the current semester
 * based on the current date.
 */
@Service
public class SemesterService {

    private final List<String> semesters = List.of("01/2024", "02/2024", "01/2025");

    /**
     * Retrieves a list of semesters spanning from three years before to two years after the current year.
     * Semesters are formatted as "1/YYYY" for the first half of the year and "2/YYYY" for the second half,
     * sorted in descending order.
     *
     * @return a list of all semesters within the specified date range, formatted as strings, in reverse chronological order.
     */
    public List<String> findAllSemester() {
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        return IntStream
                .range(year - 3, year + 2)
                .mapToObj(i -> "" + i)
                .sorted(Comparator.reverseOrder())
                .flatMap(s -> Stream.of("2/" + s, "1/" + s))
                .toList();
    }

    /**
     * Determines the current semester based on the current date.
     * If the current month is before September, returns the second semester of the previous year.
     * Otherwise, returns the first semester of the current year.
     *
     * @return a string representing the current semester, formatted as "1/YYYY" or "2/YYYY".
     */
    public String getCurrentSemester() {
        var localDate = LocalDate.now();
        var year = localDate.getYear();
        var month = localDate.getMonth();
        if (month.getValue() < Month.SEPTEMBER.getValue()) {
            year--;
            return "02/" + year;
        } else {
            return "01/" + year;
        }
    }
}
