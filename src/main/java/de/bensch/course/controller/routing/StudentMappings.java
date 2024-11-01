package de.bensch.course.controller.routing;

@SuppressWarnings("squid:S1118")
public class StudentMappings {

    public static final String STUDENT_CREATE = "/student-create";

    public static final String STUDENT_LIST = "/student-list";

    public static final String STUDENT_EDIT = "/student-edit/{id}";

    public static final String STUDENT_DELETE = "/student-delete/{id}";

    public static final String STUDENT_UPLOAD_FORM = "/student-upload-form";

    @SuppressWarnings("unused")
    public static String getStudentEditUrl(String id) {
        return STUDENT_EDIT.replace("{id}", id);
    }

    public static String redirect(String path) {
        return "redirect:" + path;
    }
}
