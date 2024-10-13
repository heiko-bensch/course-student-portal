package de.bensch.course.repository;

import de.bensch.course.model.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("""
            SELECT s FROM Student s WHERE s.semester = :semester and 
            (LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.className) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Student> findBySemesterAndKeyword(Pageable pageable, @Param("semester") String semester, @Param("keyword") String keyword);

    @Query("""
            SELECT s FROM Student s WHERE s.semester=:semester and s.gradeLevel = :gradeLevel
                AND (LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(s.className) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Student> findByGradeLevelWithKeyword(Pageable pageable, @Param("semester") String semester, @Param("gradeLevel") String gradeLevel, @Param("keyword") String keyword);

    @Query("SELECT DISTINCT s.gradeLevel FROM Student s where s.semester=:semester")
    List<String> findGradeLevel(@Param("semester") String semester);

    Page<Student> findBySemesterAndGradeLevel(Pageable pageable, String semester, String gradeLevel);


    Page<Student> findAllBySemester(Pageable pageable, String semester);
}
