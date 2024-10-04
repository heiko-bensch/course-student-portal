package de.bensch.course.model.dto;

import lombok.Data;

@Data
public class StudentCourseSelectionDTO {
    private StudentDTO student;

    private StudentCourseWeekdayDTO courseSelectionMonday = new StudentCourseWeekdayDTO();

    private StudentCourseWeekdayDTO courseSelectionTuesday = new StudentCourseWeekdayDTO();

    private StudentCourseWeekdayDTO courseSelectionWednesday = new StudentCourseWeekdayDTO();

    private StudentCourseWeekdayDTO courseSelectionThursday = new StudentCourseWeekdayDTO();
}
