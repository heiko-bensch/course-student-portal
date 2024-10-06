package de.bensch.course.model.util;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.dto.CourseSelectionDTO;
import de.bensch.course.model.dto.StudentCourseSelectionDTO;
import de.bensch.course.model.dto.StudentCourseWeekdayDTO;
import de.bensch.course.model.dto.StudentDTO;
import de.bensch.course.model.entity.Course;
import de.bensch.course.model.entity.Student;
import de.bensch.course.model.entity.StudentCourseSelection;
import org.apache.logging.log4j.util.Strings;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StudentMapper {

    // Mapping für Student zu StudentDTO
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "gradeLevel", source = "gradeLevel")
    @Mapping(target = "className", source = "className")
    StudentDTO studentToStudentDTO(Student student);


    @Mapping(target = "courseSelectionWednesday", ignore = true)
    @Mapping(target = "courseSelectionTuesday", ignore = true)
    @Mapping(target = "courseSelectionThursday", ignore = true)
    @Mapping(target = "courseSelectionMonday", ignore = true)
    @Mapping(target = "student", source = "student")
    StudentCourseSelectionDTO studentToStudentCourseSelectionDTO(Student student);


    @AfterMapping
    default void mapCourseDetails(@MappingTarget StudentCourseSelectionDTO courseSelectionDTO, Student student) {
        for (StudentCourseSelection courseSelection : student.getCourseSelections()) {
            // Manuelles Mapping für verschachtelte Felder
            StudentCourseWeekdayDTO weekdayDTO = switch (courseSelection.getWeekDay()) {
                case Monday -> courseSelectionDTO.getCourseSelectionMonday();
                case Tuesday -> courseSelectionDTO.getCourseSelectionTuesday();
                case Wednesday -> courseSelectionDTO.getCourseSelectionWednesday();
                case Thursday -> courseSelectionDTO.getCourseSelectionThursday();
            };

            var prioDTO = switch (courseSelection.getPriority()) {
                case 1 -> weekdayDTO.getCoursePrio1();
                case 2 -> weekdayDTO.getCoursePrio2();
                case 3 -> weekdayDTO.getCoursePrio3();
                default -> throw new IllegalArgumentException("Unknown Prio");

            };
            prioDTO.setId(courseSelection.getId());
            prioDTO.setComment(courseSelection.getComment());
            prioDTO.setCourseID(courseSelection.getCourse().getId());
        }
    }

    @Mapping(target = "id", source = "courseSelectionDTO.id")
    @Mapping(target = "student", source = "student") // Student direkt übergeben
    @Mapping(target = "course", expression = "java(courseLoader.loadEntityById(courseSelectionDTO.getCourseID()))")
    @Mapping(target = "weekDay", source = "dayOfWeek")
    @Mapping(target = "priority", source = "priority")
    @Mapping(target = "comment", source = "courseSelectionDTO.comment")
    StudentCourseSelection toEntity(
            Student student,
            CourseSelectionDTO courseSelectionDTO,
            WeekDay dayOfWeek,
            int priority,
            @Context EntityLoader<Course, Long> courseLoader
    );

    // Methode, um die Liste von StudentCourseSelection zu erstellen
    default List<StudentCourseSelection> toEntityList(
            StudentCourseSelectionDTO dto,
            @Context EntityLoader<Student, Long> studentLoader,
            @Context EntityLoader<Course, Long> courseLoader) {

        List<StudentCourseSelection> selections = new ArrayList<>();

        // Lade den Student aus dem Loader
        Student student = studentLoader.loadEntityById(dto.getStudent().getId());

        // Montag mappen
        selections.addAll(mapSelectionsForDay(student, dto.getCourseSelectionMonday(), WeekDay.Monday, courseLoader));

        // Dienstag mappen
        selections.addAll(mapSelectionsForDay(student, dto.getCourseSelectionTuesday(), WeekDay.Tuesday, courseLoader));

        // Mittwoch mappen
        selections.addAll(mapSelectionsForDay(student, dto.getCourseSelectionWednesday(), WeekDay.Wednesday, courseLoader));

        // Donnerstag mappen
        selections.addAll(mapSelectionsForDay(student, dto.getCourseSelectionThursday(), WeekDay.Thursday, courseLoader));

        return selections.stream()
                .filter(s -> !Objects.isNull(s.getCourse()) || !Strings.isBlank(s.getComment()) || !Objects.isNull(s.getId()))
                .toList();
    }

    // Helper-Methode für einen Tag
    default List<StudentCourseSelection> mapSelectionsForDay(
            Student student,
            StudentCourseWeekdayDTO weekdayDTO,
            WeekDay dayOfWeek,
            @Context EntityLoader<Course, Long> courseLoader) {

        List<StudentCourseSelection> selectionsForDay = new ArrayList<>();

        // Prio 1
        if (weekdayDTO.getCoursePrio1() != null) {
            selectionsForDay.add(toEntity(student, weekdayDTO.getCoursePrio1(), dayOfWeek, 1, courseLoader));
        }

        // Prio 2
        if (weekdayDTO.getCoursePrio2() != null) {
            selectionsForDay.add(toEntity(student, weekdayDTO.getCoursePrio2(), dayOfWeek, 2, courseLoader));
        }

        // Prio 3
        if (weekdayDTO.getCoursePrio3() != null) {
            selectionsForDay.add(toEntity(student, weekdayDTO.getCoursePrio3(), dayOfWeek, 3, courseLoader));
        }

        return selectionsForDay;
    }
}
