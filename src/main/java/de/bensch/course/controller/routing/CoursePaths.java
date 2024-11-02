package de.bensch.course.controller.routing;

@SuppressWarnings("squid:S1118")
public class CoursePaths {

    public static final String URL_COURSE_CREATE = "/course-create";

    public static final String URL_COURSE_LIST = "/course-list";

    public static final String URL_COURSE_EDIT = "/course-edit/{id}";

    public static final String URL_COURSE_DELETE = "/course-delete/{id}";

    public static String redirect(String path) {
        return "redirect:" + path;
    }

    @SuppressWarnings("unused")
    public String getCourseEditUrl(String id) {
        return URL_COURSE_EDIT.replace("{id}", id);
    }
}
