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

    public Page<Course> findBySemester(Pageable pageable, String semester) {
        return this.courseRepository.findBySemester(pageable, semester);
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

    public Page<Course> findBySemesterKeyword(Pageable pageable, String semester, String keyword) {
        return courseRepository.findBySemesterAndKeyword(pageable, semester, keyword);
    }

    public Iterable<Course> findBySemesterAndDayOfWeek(String semester, WeekDay weekDay) {
        return courseRepository.findBySemesterAndDayOfWeek(semester, weekDay);
    }
}
