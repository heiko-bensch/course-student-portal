package de.bensch.course.service;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.entity.Course;
import de.bensch.course.repository.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Page<Course> findAll(Pageable pageable) {
        return this.courseRepository.findAll(pageable);
    }

    public Course save(Course course) {
        return this.courseRepository.save(course);

    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public void delete(Long id) {
        this.courseRepository.deleteById(id);
    }

    public Page<Course> findByKeyword(Pageable pageable, String keyword) {
        return courseRepository.findByNameContainingIgnoreCaseOrInstructorContainingIgnoreCase(keyword, keyword, pageable);
    }

    public Iterable<Course> findByDayOfWeekday(WeekDay weekDay) {
        return courseRepository.findByDayOfWeek(weekDay);
    }
}
