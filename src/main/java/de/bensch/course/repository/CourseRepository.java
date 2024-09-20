package de.bensch.course.repository;

import de.bensch.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Iterable<Course> findByNameLikeIgnoreCase(String keyword);

    Iterable<Course> findByNameContainingIgnoreCase(String keyword);


    Iterable<Course> findByNameContainingIgnoreCaseOrInstructorContainingIgnoreCase(String keyword, String keyword1);
}
