package de.bensch.course.repository;

import de.bensch.course.model.Course;
import de.bensch.course.model.WeekDay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByNameContainingIgnoreCaseOrInstructorContainingIgnoreCase(String keyword, String keyword1, Pageable pageable);

    List<Course> findByDayOfWeek(WeekDay weekDay);
}
