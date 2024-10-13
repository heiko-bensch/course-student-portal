package de.bensch.course.repository;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Long> {


    @Query("""
                 SELECT c FROM Course c WHERE c.semester= :semester and (
                   LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                   LOWER(c.instructor) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Course> findBySemesterAndKeyword(Pageable pageable, @Param("semester") String semester, @Param("keyword") String keyword);

    List<Course> findBySemesterAndDayOfWeek(String semester, WeekDay weekDay);

    Page<Course> findBySemester(Pageable pageable, String semester);
}
