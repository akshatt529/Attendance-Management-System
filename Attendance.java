/** Attendance of ONE student in ONE subject: classes held vs attended. */
public class Attendance {
    private final Student student;
    private final Subject subject;
    private int classesHeld;
    private int classesAttended;

    public Attendance(Student student, Subject subject) {
        this(student, subject, 0, 0);
    }

    public Attendance(Student student, Subject subject, int held, int attended) {
        this.student = student;
        this.subject = subject;
        this.classesHeld = held;
        this.classesAttended = attended;
    }

    public void mark(boolean present) {
        classesHeld++;
        if (present) classesAttended++;
    }

    public double getPercentage() {
        if (classesHeld == 0) return 0.0;
        return (classesAttended * 100.0) / classesHeld;
    }

    public Student getStudent()   { return student; }
    public Subject getSubject()   { return subject; }
    public int getClassesHeld()   { return classesHeld; }
    public int getClassesAttended(){ return classesAttended; }

    @Override
    public String toString() {
        return String.format("%-8s %-20s %3d/%-3d  %6.2f%%",
                subject.getCode(), subject.getName(),
                classesAttended, classesHeld, getPercentage());
    }
}
