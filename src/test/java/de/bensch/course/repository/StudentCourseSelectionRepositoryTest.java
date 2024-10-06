package de.bensch.course.repository;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.entity.StudentCourseSelection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class StudentCourseSelectionRepositoryTest {

    @Autowired
    private StudentCourseSelectionRepository courseSelectionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;


    @Test
    public void testSaveAndFindUser() {
        var course = courseRepository.findById(1L);
        var student = studentRepository.findById(1L);
        assertThat(course).isNotEmpty();
        assertThat(student).isNotEmpty();

        var courseSelection = new StudentCourseSelection();
        courseSelection.setPriority(1);
        courseSelection.setComment("Comment");
        courseSelection.setWeekDay(WeekDay.Thursday);

        course.get().addStudentCourseSelection(courseSelection);
        student.get().addStudentCourseSelection(courseSelection);
        var result = courseSelectionRepository.save(courseSelection);

        Optional<StudentCourseSelection> courseSel = courseSelectionRepository.findById(result.getId());
        if (courseSel.isPresent()) {
            courseSelection = courseSel.get();
            assertThat(courseSel).isNotEmpty();
            assertThat(courseSelection.getCourse()).isNotNull();
            assertThat(courseSelection.getStudent()).isNotNull();
        }
    }

}
