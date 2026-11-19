
import java.sql.Connection;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Connection con = DBConnection.getConnection();

        if (con == null) {
            return;
        }

        StudentManagement sm = new StudentManagement(con);

        Scanner sc = new Scanner(System.in);

        int choice;

        do {
            System.out.println("\n===== STUDENT PERFORMANCE MANAGEMENT SYSTEM =====");

            System.out.println("1. Display Average and Performance");
            System.out.println("2. Identify Stronger Subject");
            System.out.println("3. Search Student");
            System.out.println("4. Generate Merit List");
            System.out.println("5. Find Students with Same Skills");
            System.out.println("6. Students Needing Programming Improvement");
            System.out.println("7. Add New Student");
            System.out.println("8. Exit");

            System.out.print("\nEnter Your Choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    sm.displayPerformance();
                    break;

                case 2:
                    sm.strongerSubject();
                    break;

                case 3:
                    sm.searchStudent();
                    break;

                case 4:
                    sm.meritList();
                    break;

                case 5:
                    sm.sameSkills();
                    break;

                case 6:
                    sm.improvementStudents();
                    break;

                case 7:
                    sm.addStudent();
                    break;

                case 8:
                    System.out.println("Thank You!");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 8);

        try {
            con.close();
            sc.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
