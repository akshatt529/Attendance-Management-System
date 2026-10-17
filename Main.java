import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final String FILE = "attendance.txt";
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        AttendanceManager mgr = new AttendanceManager();
        try {
            mgr.load(FILE);
        } catch (IOException e) {
            System.out.println("Could not load data: " + e.getMessage());
        }

        while (true) {
            System.out.println("\n===== Student Attendance Management System =====");
            System.out.println("1. Add student");
            System.out.println("2. Add subject");
            System.out.println("3. Take attendance for a subject (whole class)");
            System.out.println("4. Student attendance report");
            System.out.println("5. Subject attendance report");
            System.out.println("6. Shortage report (below " + (int) AttendanceManager.REQUIRED_PERCENTAGE + "%)");
            System.out.println("7. Save and exit");
            System.out.print("Choice: ");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        System.out.print("Roll no: ");
                        int roll = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Name: ");
                        mgr.addStudent(roll, sc.nextLine());
                        System.out.println("Student added.");
                        break;
                    case "2":
                        System.out.print("Subject code: ");
                        String code = sc.nextLine();
                        System.out.print("Subject name: ");
                        mgr.addSubject(code, sc.nextLine());
                        System.out.println("Subject added.");
                        break;
                    case "3":
                        takeAttendance(mgr);
                        break;
                    case "4":
                        System.out.print("Roll no: ");
                        mgr.printStudentReport(Integer.parseInt(sc.nextLine().trim()));
                        break;
                    case "5":
                        System.out.print("Subject code: ");
                        mgr.printSubjectReport(sc.nextLine());
                        break;
                    case "6":
                        mgr.printShortageReport();
                        break;
                    case "7":
                        mgr.save(FILE);
                        System.out.println("Data saved. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: please enter a valid number.");
            } catch (AttendanceException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }
    }

    private static void takeAttendance(AttendanceManager mgr) throws AttendanceException {
        System.out.print("Subject code: ");
        Subject sub = mgr.getSubject(sc.nextLine());
        System.out.println("Taking attendance for " + sub + "  (P = present, A = absent)");
        for (Student st : mgr.getAllStudents()) {
            while (true) {
                System.out.print(st + " : ");
                String in = sc.nextLine().trim().toUpperCase();
                if (in.equals("P") || in.equals("A")) {
                    mgr.markAttendance(st.getRollNo(), sub.getCode(), in.equals("P"));
                    break;
                }
                System.out.println("Enter P or A only.");
            }
        }
        System.out.println("Attendance recorded.");
    }
}
