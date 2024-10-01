CREATE TABLE student
(
    id               BIGSERIAL PRIMARY KEY,
    firstname        VARCHAR(100) NOT NULL,
    lastname         VARCHAR(100) NOT NULL,
    grade_level      VARCHAR(10)  not null,
    class_name       VARCHAR(50)  NOT NuLL,
    ballot_submitted BOOLEAN default false
);
