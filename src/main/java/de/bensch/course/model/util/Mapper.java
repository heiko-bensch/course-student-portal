package de.bensch.course.model.util;

import de.bensch.course.model.Student;
import de.bensch.course.model.StudentCourseSelection;
import de.bensch.course.model.dto.StudentCourseSelectionDTO;
import de.bensch.course.model.dto.StudentCourseWeekdayDTO;
import de.bensch.course.model.dto.StudentDTO;

import java.util.List;

public class Mapper {
    StudentCourseSelectionDTO studentCourseSelectionDTO;

    public StudentCourseSelectionDTO toDTO(Student student) {
        studentCourseSelectionDTO = new StudentCourseSelectionDTO();
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setLastName(student.getLastName());
        studentDTO.setGradeLevel(student.getGradeLevel());
        studentDTO.setClassName(student.getClassName());

        studentCourseSelectionDTO.setStudent(studentDTO);

        torte(student);


        return studentCourseSelectionDTO;
    }

    private void torte(Student student) {
        List<StudentCourseSelection> courseSelections = student.getCourseSelections();
        for (StudentCourseSelection courseSelection : courseSelections) {
            var dto = switch (courseSelection.getWeekDay()) {
                case Monday -> studentCourseSelectionDTO.getCourseSelectionMonday();
                case Tuesday -> studentCourseSelectionDTO.getCourseSelectionTuesday();
                case Wednesday -> studentCourseSelectionDTO.getCourseSelectionWednesday();
                case Thursday -> studentCourseSelectionDTO.getCourseSelectionThursday();
            };
            torte2(dto, courseSelection);
        }

    }


    private void torte2(StudentCourseWeekdayDTO courseSelectionMonday, StudentCourseSelection courseSelection) {
        var dto = switch (courseSelection.getPriority()) {
            case 1 -> courseSelectionMonday.getCoursePrio1();
            case 2 -> courseSelectionMonday.getCoursePrio2();
            case 3 -> courseSelectionMonday.getCoursePrio3();
            default -> throw new IllegalArgumentException("Unknown Prio");

        };
        dto.setComment(courseSelection.getComment());
        dto.setCourseID(courseSelection.getCourse().getId());
    }

}
