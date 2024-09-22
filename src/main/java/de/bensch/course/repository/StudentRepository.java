package de.bensch.course.repository;

import de.bensch.course.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Page<Student> findByNameContainingIgnoreCaseOrClassNameContainingIgnoreCase(String studentName, String className, Pageable pageable);
}
