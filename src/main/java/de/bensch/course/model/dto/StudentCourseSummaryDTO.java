package de.bensch.course.model.dto;

import lombok.Data;

@Data
public class StudentCourseSummaryDTO {
    private Long id;

    private String lastName;

    private String firstName;

    private String className;

    private String gradeLevel;

    private long nofCoursesOnMonday;

    private long nofCoursesOnTuesday;

    private long nofCoursesOnWednesday;

    private long nofCoursesOnThursday;

}
