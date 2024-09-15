package de.bensch.course.controller;

public class UrlMappings {

    public static final String COURSE_CREATE = "/course-create";
    public static final String COURSE_LIST = "/course-list";
    public static final String COURSE_EDIT = "/course-edit/{id}";
    public static final String COURSE_DELETE = "/course-delete/{id}";

    public String getCourseEditUrl(String id){
        return COURSE_EDIT.replace("{id}",id);

    }
    // Weitere URLs...
}
