/** A student identified by a unique roll number. */
public class Student {
    private final int rollNo;
    private final String name;

    public Student(int rollNo, String name) {
        this.rollNo = rollNo;
        this.name = name;
    }

    public int getRollNo() { return rollNo; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return rollNo + " - " + name;
    }
}
