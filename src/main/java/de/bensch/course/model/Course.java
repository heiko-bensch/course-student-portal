package de.bensch.course.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}


