package de.bensch.course.service;

import de.bensch.course.model.Student;
import de.bensch.course.model.dto.StudentCourseSelectionDTO;
import de.bensch.course.model.util.Mapper;
import de.bensch.course.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentCourseSelectionService {
    private final StudentRepository studentRepository;


    public Optional<StudentCourseSelectionDTO> findbyStudentId(Long id) {
        Mapper mapper = new Mapper();
        Optional<Student> student = studentRepository.findById(id);

        return student.map(p -> mapper.toDTO(p));
    }

}
