
import java.sql.*;
import java.util.Scanner;

public class StudentManagement {

    Connection con;
    Scanner sc = new Scanner(System.in);

    public StudentManagement(Connection con) {
        this.con = con;
    }

    // 1 and 2. Average Score and Performance Category
    public void displayPerformance() {

        try {
            String sql = "SELECT * FROM StudentPerformance";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                System.out.println("\nID: " + rs.getInt("student_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Average: " + rs.getDouble("average_score"));
                System.out.println("Category: " + rs.getString("performance_category"));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 3. Stronger Subject
    public void strongerSubject() {

        try {
            String sql = "SELECT student_id, name, " +
                    "CASE " +
                    "WHEN programming_score > mathematics_score THEN 'Programming' " +
                    "WHEN mathematics_score > programming_score THEN 'Mathematics' " +
                    "ELSE 'Both Equal' END AS stronger_subject " +
                    "FROM Students";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getInt("student_id") + " "
                        + rs.getString("name") + " "
                        + rs.getString("stronger_subject"));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 4. Search Student
    public void searchStudent() {

        System.out.println("1. Search by ID");
        System.out.println("2. Search by Email");
        System.out.println("3. Search by Course");

        int choice = sc.nextInt();
        sc.nextLine();

        String sql = "";

        if (choice == 1) {
            sql = "SELECT * FROM Students WHERE student_id = ?";
        } else if (choice == 2) {
            sql = "SELECT * FROM Students WHERE email = ?";
        } else if (choice == 3) {
            sql = "SELECT * FROM Students WHERE course = ?";
        } else {
            System.out.println("Invalid Choice");
            return;
        }

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            System.out.print("Enter Search Value: ");
            String value = sc.nextLine();

            if (choice == 1) {
                ps.setInt(1, Integer.parseInt(value));
            } else {
                ps.setString(1, value);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("\nID: " + rs.getInt("student_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Course: " + rs.getString("course"));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 5. Merit List
    public void meritList() {

        try {
            String sql = "SELECT * FROM StudentPerformance " +
                    "ORDER BY average_score DESC";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("\n----- MERIT LIST -----");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("student_id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getDouble("average_score")
                );
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 6. Students Having Same Technical Skills
    public void sameSkills() {

        try {
            String sql =
                    "SELECT skill_set, GROUP_CONCAT(student_id) AS student_ids " +
                    "FROM (" +
                    "SELECT student_id, " +
                    "GROUP_CONCAT(skill ORDER BY skill SEPARATOR ',') AS skill_set " +
                    "FROM StudentSkills GROUP BY student_id" +
                    ") AS temp " +
                    "GROUP BY skill_set HAVING COUNT(*) > 1";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                System.out.println("Students: " + rs.getString("student_ids"));
                System.out.println("Skills: " + rs.getString("skill_set"));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 7. Programming Improvement
    public void improvementStudents() {

        try {
            String sql = "SELECT * FROM Students " +
                    "WHERE programming_score < 50 " +
                    "AND mathematics_score >= 75";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getInt("student_id") + " "
                        + rs.getString("name") + " "
                        + rs.getDouble("programming_score") + " "
                        + rs.getDouble("mathematics_score"));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 8. Add Student with Validation
    public void addStudent() {

        try {
            System.out.print("Enter Student ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Course: ");
            String course = sc.nextLine();

            System.out.print("Enter Programming Score: ");
            double programming = sc.nextDouble();

            System.out.print("Enter Mathematics Score: ");
            double mathematics = sc.nextDouble();
            sc.nextLine();

            if (programming < 0 || programming > 100 ||
                    mathematics < 0 || mathematics > 100) {
                System.out.println("Scores must be between 0 and 100");
                return;
            }

            String sql = "INSERT INTO Students VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, course);
            ps.setDouble(5, programming);
            ps.setDouble(6, mathematics);

            ps.executeUpdate();

            System.out.println("Student Added Successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
