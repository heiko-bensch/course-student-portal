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
        student.setSemester("2/2024");
        return student;
    }

    public Course createCourse(WeekDay dayOfWeek, String gradeLevel) {
        Course course = new Course();
        course.setId(dataFacker.number().randomNumber());
        course.setName(dataFacker.educator().course());
        course.setInstructor(dataFacker.name().name());
        course.setDayOfWeek(dayOfWeek);
        course.setGradeLevels(gradeLevel);
        course.setSemester("2/2024");
        return course;
    }

    public Collection<StudentCourseSelection> createCourseSelections(WeekDay weekDay, int nofStudents) {
        Course course = createCourse(weekDay, "1-4");
        for (int i = 0; i < nofStudents; i++) {
            StudentCourseSelection sel = new StudentCourseSelection();
            sel.setSemester("1/2024");
            sel.setId(dataFacker.number().randomNumber());
            sel.setWeekDay(weekDay);
            sel.setPriority(dataFacker.number().numberBetween(1, 4));
            sel.setComment(dataFacker.hipster().word());
            sel.setStudent(createStudent("" + dataFacker.number().numberBetween(1, 5)));
            course.addStudentCourseSelection(sel);
        }
        return course.getCourseSelections();

    }

}
