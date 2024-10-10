package de.bensch.course.repository;

import de.bensch.course.model.entity.StudentCourseSelection;
import de.bensch.course.model.view.StudentCourseSelectionView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentCourseSelectionRepository extends JpaRepository<StudentCourseSelection, Long> {

    @Query("""
            SELECT s.id AS id,
                   s.firstName AS firstName,
                   s.lastName AS lastName,
                   s.className as className,
                   s.gradeLevel as gradeLevel,
                   SUM(CASE WHEN scMon.weekDay = de.bensch.course.model.WeekDay.Monday AND scMon.course IS NOT NULL THEN 1 ELSE 0 END) AS courseCountMonday,
                   SUM(CASE WHEN scMon.weekDay = de.bensch.course.model.WeekDay.Tuesday AND scMon.course IS NOT NULL THEN 1 ELSE 0 END) AS courseCountTuesday,
                   SUM(CASE WHEN scMon.weekDay = de.bensch.course.model.WeekDay.Wednesday AND scMon.course IS NOT NULL THEN 1 ELSE 0 END) AS courseCountWednesday,
                   SUM(CASE WHEN scMon.weekDay = de.bensch.course.model.WeekDay.Thursday AND scMon.course IS NOT NULL THEN 1 ELSE 0 END) AS courseCountThursday
            FROM Student s
            LEFT JOIN StudentCourseSelection scMon ON s.id = scMon.student.id
                GROUP BY s.id, s.firstName, s.lastName order by s.gradeLevel, s.lastName, s.firstName
            """)
    Page<StudentCourseSelectionView> findAllByStudentCourseCountByDayOfWeek(Pageable pageable);

    @Query("""
            SELECT s.id AS id,
                   s.firstName AS firstName,
                   s.lastName AS lastName,
                   s.className as className,
                   s.gradeLevel as gradeLevel,
                   SUM(CASE WHEN scMon.weekDay = de.bensch.course.model.WeekDay.Monday AND scMon.course IS NOT NULL THEN 1 ELSE 0 END) AS courseCountMonday,
                   SUM(CASE WHEN scMon.weekDay = de.bensch.course.model.WeekDay.Tuesday AND scMon.course IS NOT NULL THEN 1 ELSE 0 END) AS courseCountTuesday,
                   SUM(CASE WHEN scMon.weekDay = de.bensch.course.model.WeekDay.Wednesday AND scMon.course IS NOT NULL THEN 1 ELSE 0 END) AS courseCountWednesday,
                   SUM(CASE WHEN scMon.weekDay = de.bensch.course.model.WeekDay.Thursday AND scMon.course IS NOT NULL THEN 1 ELSE 0 END) AS courseCountThursday
            FROM Student s
                LEFT JOIN StudentCourseSelection scMon ON s.id = scMon.student.id
            WHERE s.gradeLevel = :gradeLevel
                GROUP BY s.id, s.firstName, s.lastName order by s.gradeLevel, s.lastName, s.firstName
            """)
    Page<StudentCourseSelectionView> findAllByStudentCourseCountByDayOfWeek(Pageable pageable, @Param("gradeLevel") String gradeLevel);
}
