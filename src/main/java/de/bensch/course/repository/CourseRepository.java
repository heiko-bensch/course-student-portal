package de.bensch.course.repository;

import de.bensch.course.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByNameContainingIgnoreCaseOrInstructorContainingIgnoreCase(String keyword, String keyword1, Pageable pageable);
}
