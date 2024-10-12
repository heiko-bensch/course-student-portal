package de.bensch.course.service;

import de.bensch.course.model.entity.Student;
import de.bensch.course.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;


    public void save(Student student) {
        studentRepository.save(student);
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public Page<Student> findAllBySemester(Pageable pageable, String semester) {
        return studentRepository.findAllBySemester(pageable, semester);
    }

    public Page<Student> findBySemesterAndKeyword(Pageable pageable, String semester, String keyword) {
        return studentRepository.findBySemesterAndKeyword(pageable, semester, keyword);
    }

    public void saveAll(Iterable<Student> studentList) {
        studentRepository.saveAll(studentList);
    }

    public List<String> findGradeLevel(String semester) {
        return studentRepository.findGradeLevel(semester);
    }

    public Page<Student> findAllBySemester(Pageable pageable, String semester, String classFilter) {
        return studentRepository.findBySemesterAndGradeLevel(pageable, semester, classFilter);
    }

    public Page<Student> findBySemesterAndKeyword(Pageable pageable, String semenster, String gradeLevel, String keyword) {
        return studentRepository.findByGradeLevelWithKeyword(pageable, semenster, gradeLevel, keyword);
    }
}
