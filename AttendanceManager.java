import java.io.*;
import java.util.*;

/** Core logic: stores students/subjects, marks attendance, finds shortages, saves to file. */
public class AttendanceManager {
    public static final double REQUIRED_PERCENTAGE = 75.0;

    private final Map<Integer, Student> students = new LinkedHashMap<>();
    private final Map<String, Subject> subjects = new LinkedHashMap<>();
    private final Map<String, Attendance> records = new LinkedHashMap<>();

    private static String key(int rollNo, String code) {
        return rollNo + "|" + code;
    }

    // ---------- setup ----------
    public void addStudent(int rollNo, String name) throws AttendanceException {
        if (rollNo <= 0) throw new AttendanceException("Roll number must be positive.");
        if (name == null || name.trim().isEmpty()) throw new AttendanceException("Name cannot be empty.");
        if (students.containsKey(rollNo)) throw new AttendanceException("Duplicate roll number: " + rollNo);
        students.put(rollNo, new Student(rollNo, name.trim().replace(",", " ")));
    }

    public void addSubject(String code, String name) throws AttendanceException {
        code = code.trim().toUpperCase();
        if (code.isEmpty() || name.trim().isEmpty()) throw new AttendanceException("Code and name are required.");
        if (subjects.containsKey(code)) throw new AttendanceException("Subject already exists: " + code);
        subjects.put(code, new Subject(code, name.trim().replace(",", " ")));
    }

    public Student getStudent(int rollNo) throws AttendanceException {
        Student s = students.get(rollNo);
        if (s == null) throw new AttendanceException("No student with roll number " + rollNo);
        return s;
    }

    public Subject getSubject(String code) throws AttendanceException {
        Subject s = subjects.get(code.trim().toUpperCase());
        if (s == null) throw new AttendanceException("No subject with code " + code);
        return s;
    }

    public Collection<Student> getAllStudents() { return students.values(); }
    public Collection<Subject> getAllSubjects() { return subjects.values(); }

    // ---------- attendance ----------
    private Attendance recordFor(Student st, Subject sub) {
        return records.computeIfAbsent(key(st.getRollNo(), sub.getCode()),
                k -> new Attendance(st, sub));
    }

    public void markAttendance(int rollNo, String code, boolean present) throws AttendanceException {
        Student st = getStudent(rollNo);
        Subject sub = getSubject(code);
        recordFor(st, sub).mark(present);
    }

    public double getOverallPercentage(int rollNo) throws AttendanceException {
        getStudent(rollNo);
        int held = 0, attended = 0;
        for (Attendance a : records.values()) {
            if (a.getStudent().getRollNo() == rollNo) {
                held += a.getClassesHeld();
                attended += a.getClassesAttended();
            }
        }
        return held == 0 ? 0.0 : (attended * 100.0) / held;
    }

    // ---------- reports ----------
    public void printStudentReport(int rollNo) throws AttendanceException {
        Student st = getStudent(rollNo);
        System.out.println("\nAttendance report for " + st);
        System.out.println("-------------------------------------------------------");
        boolean any = false;
        for (Attendance a : records.values()) {
            if (a.getStudent().getRollNo() == rollNo) {
                System.out.println(a + (a.getPercentage() < REQUIRED_PERCENTAGE ? "  <-- SHORTAGE" : ""));
                any = true;
            }
        }
        if (!any) System.out.println("No attendance recorded yet.");
        System.out.printf("Overall: %.2f%%%n", getOverallPercentage(rollNo));
    }

    public void printSubjectReport(String code) throws AttendanceException {
        Subject sub = getSubject(code);
        System.out.println("\nSubject report: " + sub);
        System.out.println("-------------------------------------------------------");
        for (Attendance a : records.values()) {
            if (a.getSubject().getCode().equals(sub.getCode())) {
                System.out.printf("%-5d %-15s %3d/%-3d  %6.2f%%%n",
                        a.getStudent().getRollNo(), a.getStudent().getName(),
                        a.getClassesAttended(), a.getClassesHeld(), a.getPercentage());
            }
        }
    }

    /** Students whose attendance in any subject is below the required percentage. */
    public void printShortageReport() {
        System.out.printf("%nStudents below %.0f%% attendance%n", REQUIRED_PERCENTAGE);
        System.out.println("-------------------------------------------------------");
        boolean found = false;
        for (Attendance a : records.values()) {
            if (a.getClassesHeld() > 0 && a.getPercentage() < REQUIRED_PERCENTAGE) {
                System.out.printf("%-5d %-15s %-8s %6.2f%%%n",
                        a.getStudent().getRollNo(), a.getStudent().getName(),
                        a.getSubject().getCode(), a.getPercentage());
                found = true;
            }
        }
        if (!found) System.out.println("No shortages. Everyone is above the limit.");
    }

    // ---------- file handling ----------
    public void save(String fileName) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (Student s : students.values())
                pw.println("S," + s.getRollNo() + "," + s.getName());
            for (Subject s : subjects.values())
                pw.println("J," + s.getCode() + "," + s.getName());
            for (Attendance a : records.values())
                pw.println("A," + a.getStudent().getRollNo() + "," + a.getSubject().getCode()
                        + "," + a.getClassesHeld() + "," + a.getClassesAttended());
        }
    }

    public void load(String fileName) throws IOException {
        File f = new File(fileName);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                try {
                    if (p[0].equals("S")) {
                        students.put(Integer.parseInt(p[1]), new Student(Integer.parseInt(p[1]), p[2]));
                    } else if (p[0].equals("J")) {
                        subjects.put(p[1], new Subject(p[1], p[2]));
                    } else if (p[0].equals("A")) {
                        Student st = students.get(Integer.parseInt(p[1]));
                        Subject sub = subjects.get(p[2]);
                        if (st != null && sub != null) {
                            records.put(key(st.getRollNo(), sub.getCode()),
                                    new Attendance(st, sub, Integer.parseInt(p[3]), Integer.parseInt(p[4])));
                        }
                    }
                } catch (RuntimeException e) {
                    System.out.println("Skipping corrupt line: " + line);
                }
            }
        }
    }
}
