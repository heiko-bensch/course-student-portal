package de.bensch.course.repository;

import de.bensch.course.model.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrClassNameContainingIgnoreCase(String firstName, String lastName, String className, Pageable pageable);

    @Query("""
            SELECT s FROM Student s WHERE s.gradeLevel = :gradeLevel
                AND (LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(s.className) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Student> findByGradeLevelWithKeyword(
            @Param("gradeLevel") String gradeLevel,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query("SELECT DISTINCT s.gradeLevel FROM Student s")
    List<String> findGradeLevel();

    Page<Student> findByGradeLevel(String gradeLevel, Pageable pageable);
}
