package de.bensch.course.model.entity;

import de.bensch.course.model.WeekDay;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "course_selection")
@Data
public class StudentCourseSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private WeekDay weekDay;

    @Column(name = "priority", nullable = false)
    private int priority;

    @Column(name = "comment")
    private String comment;

    @Column(name = "semester")
    private String semester;
}
