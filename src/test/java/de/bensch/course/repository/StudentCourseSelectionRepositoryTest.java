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
    void testSaveAndFindUser() {
        var course = courseRepository.findById(1L);
        var student = studentRepository.findById(1L);
        assertThat(course).isNotEmpty();
        assertThat(student).isNotEmpty();

        var courseSelection = new StudentCourseSelection();
        courseSelection.setPriority(1);
        courseSelection.setComment("Comment");
        courseSelection.setWeekDay(WeekDay.Thursday);
        courseSelection.setSemester("01/2024");

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

    @Test
    void testFindSummary() {
//        List<StudentCourseSelectionView> all = courseSelectionRepository.findAllByStudentCourseCountByDayOfWeek();
//        System.out.println("------" + all.size());
//        Optional<StudentCourseSelectionView> first = all.stream().filter(s -> s.getId() == 1L).findFirst();
//        if (first.isPresent()) {
//            System.out.println(first.get().getCourseCountMonday());
//            System.out.println(first.get().getCourseCountTuesday());
//            System.out.println(first.get().getCourseCountWednesday());
//            System.out.println(first.get().getCourseCountThursday());
//            System.out.println(first.get().getLastName());
//        }

    }


}
