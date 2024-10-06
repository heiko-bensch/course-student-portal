package de.bensch.course.model.entity;

import de.bensch.course.model.WeekDay;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "half_year")
    private Integer halfYear;

    @Column(name = "year_")
    private Integer year;

    @Column(name = "instructor")
    private String instructor;

    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private WeekDay dayOfWeek;

    @Column(name = "grade_levels")
    private String gradeLevels;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<StudentCourseSelection> courseSelections = new ArrayList<>();


    public void addStudentCourseSelection(StudentCourseSelection courseSelection) {
        courseSelections.add(courseSelection);
        courseSelection.setCourse(this); // Bidirektionale Verknüpfung
    }

}


