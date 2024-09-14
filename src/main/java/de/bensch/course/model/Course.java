package de.bensch.course.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Course {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)

    Long id;
    Integer halfYear;
    Integer year;
    String name;
    String instructor;
    String dayOfWeeg;
}


