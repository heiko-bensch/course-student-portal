CREATE TABLE course
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    day_of_week  VARCHAR(100) not null,
    instructor   VARCHAR(100),
    grade_levels VARCHAR(100),
    semester     varchar(10)  not null
);
