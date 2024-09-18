CREATE TABLE course
(
    id          INTEGER PRIMARY KEY,
    year_        SMALLINT not NULL,
    half_year   SMALLINT not Null,
    name        VARCHAR(100) NOT NULL,
    day_of_week VARCHAR(100) not null,
    instructor  VARCHAR(100),
    grade_levels VARCHAR(100)
);
