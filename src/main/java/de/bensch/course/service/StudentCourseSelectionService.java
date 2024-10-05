package de.bensch.course.service;

import de.bensch.course.model.Course;
import de.bensch.course.model.Student;
import de.bensch.course.model.StudentCourseSelection;
import de.bensch.course.model.dto.StudentCourseSelectionDTO;
import de.bensch.course.model.util.EntityLoader;
import de.bensch.course.model.util.StudentCourseSelectionMapper;
import de.bensch.course.model.util.StudentMapper;
import de.bensch.course.repository.CourseRepository;
import de.bensch.course.repository.StudentCourseSelectionRepository;
import de.bensch.course.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentCourseSelectionService {
    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;

    private final StudentCourseSelectionRepository studentCourseSelectionRepository;

    private final StudentMapper studentMapper;


    public Optional<StudentCourseSelectionDTO> findbyStudentId(Long id) {
        StudentCourseSelectionMapper mapper = new StudentCourseSelectionMapper();
        Optional<Student> student = studentRepository.findById(id);

        return student.map(studentMapper::studentToStudentCourseSelectionDTO);
    }

    @Transactional
    public void saveStudentCourseSelection(StudentCourseSelectionDTO dto) {
        EntityLoader<Student, Long> studentLoader = id ->
                studentRepository.findById(id).orElseThrow(RuntimeException::new);

        EntityLoader<Course, Long> courseLoader = id -> {
            Course course = null;
            if (id != null) {
                course = courseRepository.findById(id).orElse(null);
            }
            return course;
        };
        List<StudentCourseSelection> entityList = studentMapper.toEntityList(dto, studentLoader, courseLoader);
        for (StudentCourseSelection selection : entityList) {
            if (StringUtils.isBlank(selection.getComment()) && Objects.isNull(selection.getCourse())) {
                studentCourseSelectionRepository.delete(selection);
            } else {
                studentCourseSelectionRepository.save(selection);
            }
        }

    }


}
