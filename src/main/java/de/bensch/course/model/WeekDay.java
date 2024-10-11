package de.bensch.course.model;

import java.util.Locale;
import java.util.ResourceBundle;

public enum WeekDay {
    Monday(),
    Tuesday(),
    Wednesday(),
    Thursday();

    public String getDisplayName() {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        return bundle.getString("weekday." + this.name());
    }
}
