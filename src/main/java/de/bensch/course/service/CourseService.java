package de.bensch.course.service;

import de.bensch.course.model.Course;
import de.bensch.course.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Iterable<Course> findAll() {
        return this.courseRepository.findAll();
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

}
