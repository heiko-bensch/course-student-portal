package de.bensch.course.service.export;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.entity.Course;
import de.bensch.course.model.entity.Student;
import de.bensch.course.model.entity.StudentCourseSelection;
import net.datafaker.Faker;

import java.util.Collection;

public class StudentTestDataFactory {
    private static final Faker dataFacker = new Faker();

    public Student createStudent(String gradeLevel) {
        Student student = new Student();
        student.setId(dataFacker.number().randomNumber());
        student.setFirstName(dataFacker.name().firstName());
        student.setLastName(dataFacker.name().lastName());
        student.setClassName(dataFacker.planet().name());
        student.setGradeLevel(gradeLevel);
        return student;
    }

    public Course createCourse(WeekDay dayOfWeek, String gradeLevel) {
        Course course = new Course();
        course.setId(dataFacker.number().randomNumber());
        course.setName(dataFacker.educator().course());
        course.setInstructor(dataFacker.name().name());
        course.setYear(2024);
        course.setHalfYear(2);
        course.setDayOfWeek(dayOfWeek);
        course.setGradeLevels(gradeLevel);
        return course;
    }

    public Collection<StudentCourseSelection> createCourseSelections(WeekDay weekDay, String gradeLevel, int nofStudents) {
        Course course = createCourse(weekDay, gradeLevel);
        for (int i = 0; i < nofStudents; i++) {
            StudentCourseSelection sel = new StudentCourseSelection();
            sel.setId(dataFacker.number().randomNumber());
            sel.setWeekDay(weekDay);
            sel.setPriority(dataFacker.number().numberBetween(1, 3));
            sel.setComment(dataFacker.hipster().word());
            sel.setStudent(createStudent(gradeLevel));
            course.addStudentCourseSelection(sel);
        }
        return course.getCourseSelections();

    }

}
