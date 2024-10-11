package de.bensch.course.service;

import de.bensch.course.model.dto.StudentCourseSelectionDTO;
import de.bensch.course.model.entity.Course;
import de.bensch.course.model.entity.Student;
import de.bensch.course.model.entity.StudentCourseSelection;
import de.bensch.course.model.util.EntityLoader;
import de.bensch.course.model.util.StudentMapper;
import de.bensch.course.model.view.StudentCourseSelectionView;
import de.bensch.course.repository.CourseRepository;
import de.bensch.course.repository.StudentCourseSelectionRepository;
import de.bensch.course.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class StudentCourseSelectionService {
    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;

    private final StudentCourseSelectionRepository studentCourseSelectionRepository;

    private final StudentMapper studentMapper;


    public Optional<StudentCourseSelectionDTO> findByStudentId(Long id) {
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

    public Page<StudentCourseSelectionView> findAllByStudentCourseCountByDayOfWeek(Pageable pageable) {
        return studentCourseSelectionRepository.findAllByStudentCourseCountByDayOfWeek(pageable);
    }

    public Page<StudentCourseSelectionView> findAllByStudentCourseCountByDayOfWeek(Pageable pageable, String selectedGradeLevel) {
        return studentCourseSelectionRepository.findAllByStudentCourseCountByDayOfWeek(pageable, selectedGradeLevel);
    }

    public Optional<StudentCourseSelectionView> findNextEntry(Long id, String selectedGradeLevel) {
        List<StudentCourseSelectionView> allStudentCourse;
        if (Objects.equals("all", selectedGradeLevel)) {
            allStudentCourse = studentCourseSelectionRepository
                    .findAllByStudentCourseCountByDayOfWeek(Pageable.unpaged())
                    .getContent();
        } else {
            allStudentCourse = studentCourseSelectionRepository
                    .findAllByStudentCourseCountByDayOfWeek(Pageable.unpaged(), selectedGradeLevel)
                    .getContent();
        }
        int currentIndex = IntStream.range(0, allStudentCourse.size())
                .filter(i -> Objects.equals(allStudentCourse.get(i).getId(), id))
                .findFirst()
                .orElse(-1);
        if (currentIndex >= 0 && currentIndex < allStudentCourse.size() - 1) {
            return Optional.of(allStudentCourse.get(currentIndex + 1));
        } else {
            return Optional.empty(); // Falls kein nächster Eintrag vorhanden ist
        }
    }

    public List<StudentCourseSelection> findAll() {
        return studentCourseSelectionRepository.findAll();
    }
}
