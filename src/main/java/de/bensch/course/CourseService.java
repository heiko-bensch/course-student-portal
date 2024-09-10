package de.bensch.course;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    List<Course> getAll(){
      return  List.of(
                new Course(null,1,2024,"Kurs 1","du","MOntag"),
                new Course(null,1,2024,"Kurs 2","du","Dienstag"),
                new Course(null,1,2024,"Kurs 4","ich","Mittwoch")
        );
    }

}
