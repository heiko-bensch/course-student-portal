package de.bensch.course.model.view;

public interface StudentCourseSelectionView {
    Long getId();

    String getFirstName();

    String getLastName();

    String getClassName();

    String getGradeLevel();

    long getCourseCountMonday();

    long getCourseCountTuesday();

    long getCourseCountWednesday();

    long getCourseCountThursday();

    default boolean isOkay() {
        return getCourseCountMonday() > 0 && getCourseCountThursday() > 0 && getCourseCountWednesday() > 0 && getCourseCountTuesday() > 0;
    }
}
