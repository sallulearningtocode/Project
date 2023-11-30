import java.math.*;
import java.sql.*;
import java.util.Scanner;
public class Employee {
    private static final String url = "jdbc:sqlite:/home/aman/Documents/bankmanagement.db";
    Scanner s = new Scanner(System.in);
    public void login() {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from employees WHERE username =  ?  AND password = ? " )) {
            System.out.println("Enter Username");
            String username = s.nextLine();
            System.out.println("Enter Password");
            String password = s.nextLine(); // You need to read the name using nextLine() as next() might leave a newline character in the buffer.
    preparedStatement.setString(1,username);
    preparedStatement.setString(2,password);
   ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                System.out.println("Welcome to PNB "+resultSet.getString("fullname"));
            }
            else
            {
                System.out.println("Please Register Yourself First");
            }
        }
     catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }
    public void adminRegistration()
    {
        System.out.println("Please Enter Your Details in BLOCK LETTERS");
        System.out.println("Enter Your Name"); // 1- Name
        String fullname = s.nextLine();
        if (fullname.length() > 31) {
            System.out.println("Enter Your Name");
            fullname = s.nextLine();
        }
        s.nextLine();
        System.out.println("Enter Username"); //3 - Address
        String username = s.nextLine();
        s.nextLine();
        System.out.println("Enter Password"); //3 - Address
        String password = s.nextLine();
        System.out.println("Enter Your Phone Number"); //4 Phone Number
        BigInteger phone = s.nextBigInteger();
        if (phone.toString().length() > 10 || phone.toString().length() < 10) {
            System.out.println("Enter Your Phone Number");
            phone = s.nextBigInteger();
        }
        s.nextLine();
        System.out.println("Enter Your E-Mail Address"); //5 Email Address
        String email = s.next();
        s.nextLine();
        System.out.println("Enter Your Role");
        String role = s.nextLine();
        String sql = "INSERT INTO employees (fullname,username,password,role,email,phone) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, fullname);
            preparedStatement.setObject(2, username);
            preparedStatement.setObject(3, password);
            preparedStatement.setObject(4, role);
            preparedStatement.setObject(5, email);
            preparedStatement.setObject(6, phone);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Registration successful!");
            } else {
                System.out.println("Registration failed.");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}