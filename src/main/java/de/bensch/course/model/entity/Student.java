package de.bensch.course.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentCourseSelection> courseSelections = new ArrayList<>();

    public String getName() {
        return lastName + ", " + firstName;
    }

    public void addStudentCourseSelection(StudentCourseSelection courseSelection) {
        courseSelections.add(courseSelection);
        courseSelection.setStudent(this); // Bidirektionale Verknüpfung
    }
}
