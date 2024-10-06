CREATE TABLE course_selection
(
    id          BIGSERIAL PRIMARY KEY,
    student_id  BIGSERIAL    NOT NULL,
    course_id   BIGSERIAL    NOT NULL,
    day_of_week VARCHAR(100) not null,
    priority    INTEGER      not null,
    comment     VARCHAR(50),
    FOREIGN KEY (student_id) REFERENCES student (id),
    FOREIGN KEY (course_id) REFERENCES course (id)
);
