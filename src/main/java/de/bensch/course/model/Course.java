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
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "half_year")
    Integer halfYear;

    @Column(name = "year_")
    Integer year;


    @Column(name = "instructor")
    String instructor;

    @Column(name = "day_of_week")
    String dayOfWeek;

}


