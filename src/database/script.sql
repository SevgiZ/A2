CREATE TABLE students (
username TEXT NOT NULL UNIQUE,
student_id TEXT PRIMARY KEY UNIQUE,
first_name TEXT NOT NULL,
last_name TEXT NOT NULL,
password TEXT NOT NULL
);

CREATE TABLE courses (
course_id INTEGER PRIMARY KEY,
course_name TEXT NOT NULL,
capacity TEXT NOT NULL,
open_closed TEXT NOT NULL,
year TEXT NOT NULL,
delivery_mode TEXT NOT NULL,
day_of_lecture TEXT NOT NULL,
time_of_lecture TEXT NOT NULL,
duration_of_lecture DOUBLE NOT NULL,
dates TEXT NOT NULL
);

CREATE TABLE student_enrolled_courses (
enrolled_course_num INTEGER PRIMARY KEY AUTOINCREMENT,
student_id TEXT NOT NULL,
course_id INTEGER NOT NULL,
FOREIGN KEY (student_id) REFERENCES students (student_id),
FOREIGN KEY (course_id) REFERENCES courses (id)
);