package de.bensch.course.model.util;

import de.bensch.course.model.Course;
import de.bensch.course.model.Student;
import de.bensch.course.model.StudentCourseSelection;
import de.bensch.course.model.WeekDay;
import de.bensch.course.model.dto.StudentCourseSelectionDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MapperTest {

    @Test
    void testMapStudent() {
        var sel = createStudentCourseSelection();

        Mapper mapper = new Mapper();
        StudentCourseSelectionDTO dto = mapper.toDTO(sel.getStudent());

        var studentDTO = dto.getStudent();
        assertThat(studentDTO).isNotNull();
        assertThat(studentDTO.getId()).isEqualTo(42L);
        assertThat(studentDTO.getClassName()).isEqualTo("Classname");
        assertThat(studentDTO.getFirstName()).isEqualTo("FirstName");
        assertThat(studentDTO.getLastName()).isEqualTo("LastName");
        assertThat(studentDTO.getGradeLevel()).isEqualTo("1111");


    }

    @Test
    void testMapMondayCourse() {
        var sel = createStudentCourseSelection();

        Mapper mapper = new Mapper();
        StudentCourseSelectionDTO dto = mapper.toDTO(sel.getStudent());

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
    void testMapThursdayCourse() {
        var sel = createStudentCourseSelection();

        Mapper mapper = new Mapper();
        StudentCourseSelectionDTO dto = mapper.toDTO(sel.getStudent());

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

        StudentCourseSelection selection = new StudentCourseSelection();
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

}
