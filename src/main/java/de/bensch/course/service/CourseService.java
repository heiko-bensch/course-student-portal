package de.bensch.course.service;

import de.bensch.course.model.Course;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CourseService {
    private List<Course> courses =  new ArrayList<>(List.of(
            new Course(1L,1,2024,"Kurs 1","du","Montag"),
            new Course(2L,1,2024,"Kurs 2","du","Dienstag"),
            new Course(3L,1,2024,"Kurs 4","ich","Mittwoch")
    ));

    public List<Course> findAll(){
      return courses;
    }

    public Course save(Course course) {
        if(course.getId()==null){
            long i = courses.size() + 1;
            course.setId(i);
            courses.add(course);
        }else{
            Optional<Course> first = courses
                    .stream()
                    .filter(c -> Objects.equals(c.getId(), course.getId()))
                    .findFirst();
            if (first.isPresent()) {
                courses.remove(first.get());
                this.courses.add(course);
            }
        }
        return course;
    }

    public Optional<Course> findById(Long id) {
        return courses
                .stream()
                .filter(c -> Objects.equals(c.getId(), id))
                .findFirst();
    }

    public void delete(Course course) {
        courses.remove(course);
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>();
    }
}
