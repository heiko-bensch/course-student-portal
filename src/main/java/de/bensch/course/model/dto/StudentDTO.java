package de.bensch.course.model.dto;

import lombok.Data;

@Data
public class StudentDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private String className;

    private String gradeLevel;
}
