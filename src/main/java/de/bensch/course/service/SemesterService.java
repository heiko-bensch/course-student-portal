package de.bensch.course.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemesterService {

    private final List<String> semesters = List.of("01/2024", "02/2024", "01/2025");

    public List<String> findAll() {
        return semesters;
    }


}
