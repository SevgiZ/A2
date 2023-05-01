CREATE TABLE students (
student_id TEXT PRIMARY KEY UNIQUE,
first_name TEXT NOT NULL,
last_name TEXT NOT NULL,
username TEXT NOT NULL UNIQUE
);

CREATE TABLE courses (
course_id INTEGER PRIMARY KEY,
course_name TEXT NOT NULL,
capacity TEXT NOT NULL,
year TEXT NOT NULL,
delivery_mode TEXT NOT NULL,
day_of_lecture TEXT NOT NULL,
time_of_lecture TEXT NOT NULL,
duration_of_lecture DOUBLE NOT NULL
);


CREATE TABLE student_courses (
student_id TEXT NOT NULL,
course_id INTEGER NOT NULL,
PRIMARY KEY (student_id, course_id),
FOREIGN KEY (student_id) REFERENCES students (id),
FOREIGN KEY (course_id) REFERENCES courses (id)
);