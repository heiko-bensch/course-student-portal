package de.bensch.course.repository;

import de.bensch.course.model.StudentCourseSelection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCourseSelectionRepository extends JpaRepository<StudentCourseSelection, Long> {
    
}
