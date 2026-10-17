/** Custom exception for invalid attendance operations
 *  (duplicate roll number, unknown student/subject, bad input). */
public class AttendanceException extends Exception {
    public AttendanceException(String message) {
        super(message);
    }
}
