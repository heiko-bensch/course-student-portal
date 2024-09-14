package de.bensch.course.service;

import de.bensch.course.model.Student;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
       private final List<Student> students = new ArrayList<>(
         List.of(
                 new Student(1L,"name1",1,"klasse1"),
                 new Student(2L,"name2",2,"klasse2"),
                 new Student(3L,"name3",1,"klasse1")
         )
       );
    public List<Student> findAll() {
        return students;
    }

    public void save(Student student) {
    }

    public Optional<Student> findById(Long id) {
        return Optional.empty();
    }

    public void delete(Student student) {
    }

    public List<String> getAllClassNames() {
     return     List.of("klasse1","klasse2");
    }

    public List<Student> findStudentsByClassNameAndNameContaining(String className, String queryName) {
       return findAll().stream()
               .filter(student -> Objects.equals(className,student.getClassName()))
               .filter(student -> student.getClassName().contains(queryName))
               .collect(Collectors.toList());
    }



    public void assignCourseToStudent(Long studentId, Long courseId) {

    }
}
