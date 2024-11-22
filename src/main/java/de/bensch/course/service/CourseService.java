package de.bensch.course.service;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.entity.Course;
import de.bensch.course.repository.CourseRepository;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {
    private static final String SEMESTER_MUST_NOT_BE_NULL = "Semester must not be null";
    private static final String PAGEABLE_MUST_NOT_BE_NULL = "Pageable must not be null";

    private final CourseRepository courseRepository;

    @Nonnull
    public Page<Course> findBySemester(@Nonnull Pageable pageable, @Nonnull String semester) {
        Objects.requireNonNull(pageable, PAGEABLE_MUST_NOT_BE_NULL);
        Objects.requireNonNull(semester, SEMESTER_MUST_NOT_BE_NULL);
        return this.courseRepository.findBySemester(pageable, semester);
    }

    @Nonnull
    public Course save(@Nonnull Course course) {
        Objects.requireNonNull(course, "Course must not be null");
        return this.courseRepository.save(course);
    }

    @Nonnull
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public void delete(@Nonnull Long id) {
        this.courseRepository.deleteById(id);
    }

    @Nonnull
    public Page<Course> findBySemesterKeyword(@Nonnull Pageable pageable, @Nonnull String semester, @Nonnull String keyword) {
        Objects.requireNonNull(pageable, PAGEABLE_MUST_NOT_BE_NULL);
        Objects.requireNonNull(semester, SEMESTER_MUST_NOT_BE_NULL);
        Objects.requireNonNull(keyword, "Keyword must not be null");
        return courseRepository.findBySemesterAndKeyword(pageable, semester, keyword);
    }

    @Nonnull
    public Iterable<Course> findBySemesterAndDayOfWeek(@Nonnull String semester, @Nonnull WeekDay weekDay) {
        Objects.requireNonNull(semester, SEMESTER_MUST_NOT_BE_NULL);
        Objects.requireNonNull(weekDay, "WeekDay must not be null");
        return courseRepository.findBySemesterAndDayOfWeek(semester, weekDay);
    }
}
