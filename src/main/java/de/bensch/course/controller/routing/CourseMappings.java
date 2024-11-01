package de.bensch.course.controller.routing;

@SuppressWarnings("squid:S1118")
public class CourseMappings {

    public static final String COURSE_CREATE = "/course-create";

    public static final String COURSE_LIST = "/course-list";

    public static final String COURSE_EDIT = "/course-edit/{id}";

    public static final String COURSE_DELETE = "/course-delete/{id}";

    public static String redirect(String path) {
        return "redirect:" + path;
    }

    @SuppressWarnings("unused")
    public String getCourseEditUrl(String id) {
        return COURSE_EDIT.replace("{id}", id);
    }
}
