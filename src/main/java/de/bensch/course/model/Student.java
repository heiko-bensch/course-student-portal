package de.bensch.course.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Student{
    Long id;
    String name;
    int clazz;
    String className;
}
