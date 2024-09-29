package de.bensch.course.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "grade_level")
    private String gradeLevel;

    @Column(name = "class_name")
    private String className;

    @Column(name = "ballot_submitted")
    private boolean ballotSubmitted;

    public String getName() {
        return lastName + ", " + firstName;
    }
}
