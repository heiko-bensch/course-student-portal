package de.bensch.course.model.util;

import de.bensch.course.model.Course;
import de.bensch.course.model.Student;
import de.bensch.course.model.StudentCourseSelection;
import de.bensch.course.model.WeekDay;
import de.bensch.course.model.dto.StudentCourseSelectionDTO;
import de.bensch.course.model.dto.StudentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class StudentMapperTest {

    @Autowired
    private StudentMapper studentMapper;

    EntityLoader<Student, Long> studentLoader;

    EntityLoader<Course, Long> courseLoader;

    @BeforeEach
    void setuo() {
        studentLoader = id -> {
            var s = new Student();
            s.setId(id);
            return s;
        };


        courseLoader = id -> {
            Course c = null;

            if (id != null) {
                c = new Course();
                c.setId(id);
            }
            return c;
        };
    }

    @Test
    void mapToStudentSelectionDTO() {
        var sel = createStudentCourseSelection();

        StudentCourseSelectionDTO dto = studentMapper.studentToStudentCourseSelectionDTO(sel.getStudent());

        var studentDTO = dto.getStudent();
        assertThat(studentDTO).isNotNull();
        assertThat(studentDTO.getId()).isEqualTo(42L);
        assertThat(studentDTO.getClassName()).isEqualTo("Classname");
        assertThat(studentDTO.getFirstName()).isEqualTo("FirstName");
        assertThat(studentDTO.getLastName()).isEqualTo("LastName");
        assertThat(studentDTO.getGradeLevel()).isEqualTo("1111");


    }

    @Test
    void mapToStudentSelectionDTOwithMondayCourse() {
        var sel = createStudentCourseSelection();

        StudentCourseSelectionDTO dto = studentMapper.studentToStudentCourseSelectionDTO(sel.getStudent());

        var studentCourseWeekdayDTO = dto.getCourseSelectionMonday();
        assertThat(studentCourseWeekdayDTO).isNotNull();
        assertThat(studentCourseWeekdayDTO.getCoursePrio1()).isNotNull();
        assertThat(studentCourseWeekdayDTO.getCoursePrio1().getCourseID()).isEqualTo(43L);
        assertThat(studentCourseWeekdayDTO.getCoursePrio1().getComment()).isEqualTo("Comment1");

        assertThat(studentCourseWeekdayDTO.getCoursePrio2()).isNotNull();
        assertThat(studentCourseWeekdayDTO.getCoursePrio2().getCourseID()).isNull();

        assertThat(studentCourseWeekdayDTO.getCoursePrio3()).isNotNull();
        assertThat(studentCourseWeekdayDTO.getCoursePrio3().getCourseID()).isNull();

    }

    @Test
    void mapToStudentSelectionDTOwithThursdayCourse() {
        var sel = createStudentCourseSelection();

        StudentCourseSelectionDTO dto = studentMapper.studentToStudentCourseSelectionDTO(sel.getStudent());

        var studentCourseWeekdayDTO = dto.getCourseSelectionThursday();
        assertThat(studentCourseWeekdayDTO).isNotNull();
        assertThat(studentCourseWeekdayDTO.getCoursePrio1()).isNotNull();
        assertThat(studentCourseWeekdayDTO.getCoursePrio1().getCourseID()).isNull();
        assertThat(studentCourseWeekdayDTO.getCoursePrio1().getComment()).isNull();

        assertThat(studentCourseWeekdayDTO.getCoursePrio2()).isNotNull();
        assertThat(studentCourseWeekdayDTO.getCoursePrio2().getCourseID()).isEqualTo(43L);

        assertThat(studentCourseWeekdayDTO.getCoursePrio3()).isNotNull();
        assertThat(studentCourseWeekdayDTO.getCoursePrio3().getCourseID()).isNull();


    }

    private static StudentCourseSelection createStudentCourseSelection() {
        Student student = new Student();
        student.setId(42L);
        student.setClassName("Classname");
        student.setFirstName("FirstName");
        student.setLastName("LastName");
        student.setGradeLevel("1111");
        student.setBallotSubmitted(true);

        Course course = new Course();
        course.setId(43L);
        course.setYear(2021);
        course.setGradeLevels("1222");
        course.setName("Course 1");
        course.setDayOfWeek(WeekDay.Monday);
        course.setInstructor("Instructor");
        course.setHalfYear(1);

        StudentCourseSelection selection;
        selection = new StudentCourseSelection();
        selection.setWeekDay(WeekDay.Monday);
        selection.setPriority(1);
        selection.setComment("Comment1");
        course.addStudentCourseSelection(selection);
        student.addStudentCourseSelection(selection);

        selection = new StudentCourseSelection();
        selection.setWeekDay(WeekDay.Thursday);
        selection.setPriority(2);
        selection.setComment("Comment2");

        course.addStudentCourseSelection(selection);
        student.addStudentCourseSelection(selection);
        return selection;
    }


    @Test
    void mapToStudentCourseSelection() {
        var dto = new StudentCourseSelectionDTO();
        var student = new StudentDTO();
        student.setId(34L);
        dto.setStudent(student);

        //was changed by the user
        dto.getCourseSelectionMonday().getCoursePrio1().setId(111L);
        dto.getCourseSelectionMonday().getCoursePrio1().setCourseID(112L);
        dto.getCourseSelectionMonday().getCoursePrio1().setComment("comment113");
        //newly created by the user
        dto.getCourseSelectionMonday().getCoursePrio2().setCourseID(123L);

        dto.getCourseSelectionTuesday().getCoursePrio3().setId(231L);

        //was changed by the user
        dto.getCourseSelectionWednesday().getCoursePrio1().setId(311L);
        dto.getCourseSelectionWednesday().getCoursePrio1().setComment("comment312");

        //delete by the user
        dto.getCourseSelectionWednesday().getCoursePrio2().setId(321L);


        List<StudentCourseSelection> entityList = studentMapper.toEntityList(dto, studentLoader, courseLoader);


        List<StudentCourseSelection> expectedResult = new ArrayList<>();
        expectedResult.add(createStudentCourseSelection(111L, 34L, 112L, WeekDay.Monday, 1, "comment113"));
        expectedResult.add(createStudentCourseSelection(null, 34L, 123L, WeekDay.Monday, 2, null));

        expectedResult.add(createStudentCourseSelection(231L, 34L, null, WeekDay.Tuesday, 3, null));

        expectedResult.add(createStudentCourseSelection(311L, 34L, null, WeekDay.Wednesday, 1, "comment312"));

        expectedResult.add(createStudentCourseSelection(321L, 34L, null, WeekDay.Wednesday, 2, null));


        assertThat(entityList).containsExactlyInAnyOrderElementsOf(expectedResult);

    }


    StudentCourseSelection createStudentCourseSelection(Long id, Long studentID, Long courseID, WeekDay weekDay, int prio, String comment) {
        Student student = null;
        Course course = null;
        if (studentID != null) {
            student = new Student();
            student.setId(studentID);
        }
        if (courseID != null) {
            course = new Course();
            course.setId(courseID);
        }
        var studentCourseSelection = new StudentCourseSelection();
        studentCourseSelection.setId(id);
        studentCourseSelection.setStudent(student);
        studentCourseSelection.setCourse(course);
        studentCourseSelection.setWeekDay(weekDay);
        studentCourseSelection.setPriority(prio);
        studentCourseSelection.setComment(comment);
        return studentCourseSelection;

    }


}
