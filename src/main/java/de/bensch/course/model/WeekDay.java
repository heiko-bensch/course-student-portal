package de.bensch.course.model;

import java.util.Locale;
import java.util.ResourceBundle;

public enum WeekDay {
    MONDAY(),
    TUESDAY(),
    WEDNESDAY(),
    THURSDAY();

    public String getDisplayName() {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        return bundle.getString("weekday." + this.name());
    }
}
