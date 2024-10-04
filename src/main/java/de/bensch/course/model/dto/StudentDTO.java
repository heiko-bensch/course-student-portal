package de.bensch.course.model.dto;

import lombok.Data;

@Data
public class StudentDTO {
    Long id;

    String firstName;

    String lastName;

    String className;

    String gradeLevel;
}
