/** A subject/course for which attendance is taken. */
public class Subject {
    private final String code;
    private final String name;

    public Subject(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() { return code; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return code + " - " + name;
    }
}
