# Student Attendance Management System (Java, OOP)

## Problem
Record attendance for each subject, calculate attendance percentages and
identify students below the required threshold (75%). Data is saved to a
text file so it survives between runs.

## Classes
| Class | Role |
|---|---|
| Student | Roll number and name |
| Subject | Subject code and name |
| Attendance | One student + one subject: classes held / attended, percentage |
| AttendanceManager | Core logic: add, mark, reports, shortage list, file save/load |
| AttendanceException | Custom exception (duplicate roll no, unknown student/subject, bad input) |
| Main | Console menu |

## OOP concepts used
- Encapsulation: private fields, logic inside AttendanceManager
- Association/composition: Attendance links a Student and a Subject
- Custom exception handling, plus NumberFormatException and IOException
- Collections: LinkedHashMap for fast lookup by roll no / subject code
- File handling: save() / load() using FileWriter and BufferedReader

## Run
    javac *.java
    java Main
Suggested demo: add 3 students, add 2 subjects, take attendance 4-5 times
with different P/A answers, then view reports 4, 5 and 6.

## Extension ideas
Date-wise attendance, edit/correct attendance, export to CSV,
different thresholds per subject, GUI with Swing, RFID/biometric input.
