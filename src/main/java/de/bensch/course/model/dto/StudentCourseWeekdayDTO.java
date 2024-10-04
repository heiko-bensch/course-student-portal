package de.bensch.course.model.dto;

import lombok.Data;

@Data
public class StudentCourseWeekdayDTO {

    private CourseSelectionDTO coursePrio1 = new CourseSelectionDTO();

    private CourseSelectionDTO coursePrio2 = new CourseSelectionDTO();

    private CourseSelectionDTO coursePrio3 = new CourseSelectionDTO();

}
