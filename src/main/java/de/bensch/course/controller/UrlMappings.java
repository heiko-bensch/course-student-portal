package de.bensch.course.controller;

public class UrlMappings {

    public static final String COURSE_CREATE = "/course-create";

    public static final String COURSE_LIST = "/course-list";

    public static final String COURSE_EDIT = "/course-edit/{id}";

    public static final String COURSE_DELETE = "/course-delete/{id}";

    public static final String STUDENT_CREATE = "/student-create";

    public static final String STUDENT_LIST = "/student-list";

    public static final String STUDENT_EDIT = "/student-edit/{id}";

    public static final String STUDENT_DELETE = "/student-delete/{id}";

    public static final String STUDENT_UPLOAD_FORM = "/student-upload-form";

    public static final String STUDENT_COURSE_ASSIGNMENT = "/student-course-assignment";

    public static final String STUDENT_COURSE_LIST = "/student-course-list";

    public String getCourseEditUrl(String id) {
        return COURSE_EDIT.replace("{id}", id);
    }

    public String getStudentEditUrl(String id) {
        return STUDENT_EDIT.replace("{id}", id);
    }
}
