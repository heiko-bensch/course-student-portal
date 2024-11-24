package de.bensch.course.service;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.entity.Course;
import de.bensch.course.repository.CourseRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;


    @Test
    void testFindBySemester() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        String semester = "2024";
        Page<Course> expectedPage = new PageImpl<>(List.of(new Course(), new Course()));

        when(courseRepository.findBySemester(pageable, semester)).thenReturn(expectedPage);

        // Act
        Page<Course> result = courseService.findBySemester(pageable, semester);

        // Assert
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result).isNotNull();
        softly.assertThat(result.getTotalElements()).isEqualTo(2);
        softly.assertAll();
        verify(courseRepository, times(1)).findBySemester(pageable, semester);
    }

    @Test
    void testFindBySemester_ShouldThrowIfSemesterIsNull() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> courseService.findBySemester(pageable, null));

        assertThat(exception.getMessage()).isEqualTo("Semester must not be null");
    }

    @Test
    void testSave() {
        // Arrange
        Course course = new Course();
        when(courseRepository.save(course)).thenReturn(course);

        // Act
        Course savedCourse = courseService.save(course);

        // Assert
        assertThat(savedCourse).isNotNull();
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testSave_ShouldThrowIfCourseIsNull() {
        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> courseService.save(null));

        assertThat(exception.getMessage()).isEqualTo("Course must not be null");
    }

    @Test
    void testFindById() {
        // Arrange
        Long courseId = 1L;
        Course course = new Course();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        Optional<Course> result = courseService.findById(courseId);

        // Assert
        assertThat(result).isPresent();
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void testDelete() {
        // Arrange
        Long courseId = 1L;

        // Act
        courseService.delete(courseId);

        // Assert
        verify(courseRepository, times(1)).deleteById(courseId);
    }

    @Test
    void testFindBySemesterKeyword() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        String semester = "2024";
        String keyword = "Math";
        Page<Course> expectedPage = new PageImpl<>(List.of(new Course(), new Course()));

        when(courseRepository.findBySemesterAndKeyword(pageable, semester, keyword)).thenReturn(expectedPage);

        // Act
        Page<Course> result = courseService.findBySemesterKeyword(pageable, semester, keyword);

        // Assert
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result).isNotNull();
        softly.assertThat(result.getTotalElements()).isEqualTo(2);
        softly.assertAll();
        verify(courseRepository, times(1)).findBySemesterAndKeyword(pageable, semester, keyword);
    }

    @Test
    void testFindBySemesterKeyword_ShouldThrowIfKeywordIsNull() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        String semester = "2024";

        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> courseService.findBySemesterKeyword(pageable, semester, null));
        assertThat(exception.getMessage()).isEqualTo("Keyword must not be null");
    }

    @Test
    void testFindBySemesterAndDayOfWeek() {
        // Arrange
        String semester = "2024";
        WeekDay weekDay = WeekDay.MONDAY;
        List<Course> courses = List.of(new Course(), new Course());

        when(courseRepository.findBySemesterAndDayOfWeek(semester, weekDay)).thenReturn(courses);

        // Act
        Iterable<Course> result = courseService.findBySemesterAndDayOfWeek(semester, weekDay);

        // Assert
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(List.class);
        softly.assertThat(((List<Course>) result)).hasSize(2);
        softly.assertAll();
        verify(courseRepository, times(1)).findBySemesterAndDayOfWeek(semester, weekDay);
    }

    @Test
    void testFindBySemesterAndDayOfWeek_ShouldThrowIfWeekDayIsNull() {
        // Arrange
        String semester = "2024";

        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> {
            courseService.findBySemesterAndDayOfWeek(semester, null);
        });

        assertThat(exception.getMessage()).isEqualTo("WeekDay must not be null");
    }
}
