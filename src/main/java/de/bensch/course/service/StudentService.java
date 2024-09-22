package de.bensch.course.service;

import de.bensch.course.model.Student;
import de.bensch.course.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Page<Student> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public void save(Student student) {
        studentRepository.save(student);
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public List<String> getAllClassNames() {
        return List.of("klasse1", "klasse2");
    }


    public Page<Student> findByKeyword(Pageable pageable, String keyword) {
        return studentRepository.findByNameContainingIgnoreCaseOrClassNameContainingIgnoreCase(keyword, keyword, pageable);
    }
}
