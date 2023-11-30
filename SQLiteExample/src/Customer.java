import java.math.*;
import java.sql.*;
import java.util.Scanner;
public class Customer {
    private static final String url = "jdbc:sqlite:/home/aman/Documents/bankmanagement.db";
    Scanner s = new Scanner(System.in);
    public void login() {
        System.out.println("Enter Your Phone");
        BigInteger phone = s.nextBigInteger();
        s.nextLine();
        System.out.println("Enter Your Name");
        String name = s.nextLine();
        String sql ="SELECT * from customers WHERE phone = ? AND name = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1,phone);
            preparedStatement.setString(2,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet.getInt("customerid"));
            while (resultSet.next()) {
                int customerid = resultSet.getInt("customerid");
                String customerName = resultSet.getString("name");
                int customerAge = resultSet.getInt("age");
                System.out.println("ID: " + customerid + ", Name: " + customerName + ", Age: " + customerAge);
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }
    public void debitCardForm()
    {
        System.out.println("Enter Account Number");
        BigInteger acno = s.nextBigInteger();
        System.out.println("Enter Customer Id");
        int customerid = s.nextInt();
        String sql ="SELECT * from customers WHERE accno = ? AND customerid = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement selectStatement = connection.prepareStatement(sql)) {
            selectStatement.setObject(1, acno);
            selectStatement.setObject(2, customerid);
            ResultSet resultSet = selectStatement.executeQuery();
//            acno = BigInteger.valueOf(resultSet.getInt("accno"));
            String name = resultSet.getString("name");
            System.out.println(acno);

            if(resultSet.next())
            {
                System.out.println("SET 4 Digits Pin");
                int pin = s.nextInt();
                System.out.println("Enter Again");
                pin = s.nextInt();
//                9551764349
                BigInteger debitcardno = BigInteger.valueOf(generateRandom10DigitNumber());
                System.out.println("Account Number " + acno);
                System.out.println("Name " + name);
                System.out.println("Debit Card Number " + debitcardno);
                String insertSql = "INSERT into debitcardusers (accno,name,debitcardno,pin,status) VALUES (?,?,?,?,?)";
            try(PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                insertStatement.setObject(1, acno);
                insertStatement.setObject(2, name);
                insertStatement.setObject(3, debitcardno);
                insertStatement.setObject(4, pin);
                insertStatement.setObject(5, "Applied");

                int rowsInserted = insertStatement.executeUpdate();
                if(rowsInserted>0){
                    System.out.println("Registration Completed");
            }   else{
                    System.out.println("Registration Failed");
                }

            }
            }

        }
        catch(SQLException e)
        {
            System.out.println("SQL EXCEPTION"+e.getMessage());
        }

    }
    public void debit()
    {
        System.out.println();
        System.out.println("Enter Debit Card No.");
        BigInteger debitcardno = s.nextBigInteger();
        String sql ="SELECT * from debitcardusers WHERE debitcardno = ? ";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1,debitcardno);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                System.out.println("Enter Pin");
                int pin = s.nextInt();
                if(pin==resultSet.getInt("pin"))
                {
                    String fetchdetails = "SELECT * from customers WHERE accno = ?";
                   try(Connection connection1 = DriverManager.getConnection(url);
                    PreparedStatement fetchStatement = connection.prepareStatement(fetchdetails)){
                       BigInteger accno = BigInteger.valueOf(resultSet.getLong("accno"));
                        fetchStatement.setObject(1,accno);
                        ResultSet resultSet1 = fetchStatement.executeQuery();
                    System.out.println("Enter Amount");
                    long amount = s.nextLong();
                    if(amount< resultSet1.getLong("amount"))
                    {
                        System.out.println("Transaction Successful");
                        Long remainingBalance = (resultSet1.getLong("amount")-amount);
                        String update = "UPDATE  customers SET amount =? WHERE accno = ?";
                        PreparedStatement updateAmount = connection.prepareStatement(update);
                            updateAmount.setObject(1,BigInteger.valueOf(remainingBalance));
                            updateAmount.setObject(2,accno);
                            int resultSet2 = updateAmount.executeUpdate();
            connection.close();
                        connection1.close();
                        updateTransaction(BigInteger.valueOf(amount),accno,"ATM","debit");
                                System.out.println("Flag");
                        System.out.println(resultSet2);
                    }
                }
                }
            }
        }
        catch(SQLException e)
        {
            System.out.println("SQL EXCEPTION"+e.getMessage());
        }

    }
    public void credit()
    {
        System.out.println("Enter Account Number");
        BigInteger accno = s.nextBigInteger();
        System.out.println("Enter Amount");
        BigInteger amount = s.nextBigInteger();
        String creditSql = "UPDATE customers SET amount = amount + ? WHERE accno = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(creditSql)) {
            preparedStatement.setObject(1,amount);
            preparedStatement.setObject(2,accno);
            int resultSet = preparedStatement.executeUpdate();
             System.out.println("Flag in Credit");
            connection.close();
            System.out.println("Credit Connection Closed");
            if(resultSet>0)
            {
                System.out.println("INSIDE RESULTSET > 0");
                updateTransaction(amount,accno,"netbanking","credit");
            }
    }
        catch(SQLException e)
        {
            System.out.println("SQL EXCEPTION "+e.getMessage());
        }
    }
    public  void updateTransaction(BigInteger amount, BigInteger accno, String medium,String type) throws SQLException {
        System.out.println("updateTransaction");
        Long balance = fetchBalance(accno);
        String transactionSql = "INSERT into  transactions (accno,via,amount,transaction_type,transaction_date,balance) VALUES (?,?,?,?,CURRENT_TIMESTAMP,?)";
        System.out.println("Flag");
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(transactionSql)) {
            preparedStatement.setObject(1,accno);
            preparedStatement.setObject(2,medium);
            preparedStatement.setObject(3,amount);
            preparedStatement.setObject(4,type);
            preparedStatement.setObject(5,balance);
            int resultSet = preparedStatement.executeUpdate();
            System.out.println("UpdateTransaction Connection Closed");
            if(resultSet>0)
            {
                System.out.println("Party ALL Night");
            }
    }
        catch (SQLException e)
        {
            System.out.println("SQL EXCEPTION"+e.getMessage());
        }
    }
    public void accountOpenForm()
    {
        System.out.println("Please Enter Your Details in BLOCK LETTERS");
        System.out.println("Enter Your Name"); // 1- Name
        String name = s.nextLine();
        if (name.length() > 31) {
            System.out.println("Enter Your Name");
            name = s.nextLine();
        }
        System.out.println("Enter Your Age"); //2 Age
        int age = s.nextInt();
        while(age<18||age>120)
        {
            System.out.println("Your Age must be in range 18-120");
            age=s.nextInt();
        }
        s.nextLine();
        System.out.println("Enter Your Address"); //3 - Address
        String address = s.nextLine();
        System.out.println("Enter Your Phone Number"); //4 Phone Number
        BigInteger phone = s.nextBigInteger();
        if (phone.toString().length() > 10 || phone.toString().length() < 10) {
            System.out.println("Enter Your Phone Number");
            phone = s.nextBigInteger();
        }
        s.nextLine();
        System.out.println("Enter Your E-Mail Address"); //5 Email Address
        String email = s.next();
        System.out.println("Enter Adhaar Number"); //6 Adhaar
        BigInteger adhaar = s.nextBigInteger();
        s.nextLine();
        System.out.println("Enter Pan Number");//7 Pan
        String panno = s.next();
        s.nextLine();
        System.out.println("Enter Your Profession");//8 Profession
        String profession = s.next();
        BigInteger accno = BigInteger.valueOf(generateRandom10DigitNumber());
        System.out.println("Enter Account Opening Amount");
        BigInteger amount = s.nextBigInteger();
        String sql = "INSERT INTO customers (accno,actype,name,age,address,phone, email, adhaar,pan,profession,acstatus,amount) VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setString(1, String.valueOf(customerid);
            preparedStatement.setObject(1, accno);
            preparedStatement.setObject(2, "Savings");
            preparedStatement.setObject(3, name);
            preparedStatement.setObject(4, age);
            preparedStatement.setObject(5, address);
            preparedStatement.setObject(6, phone);
            preparedStatement.setString(7, email);
            preparedStatement.setObject(8, adhaar);
            preparedStatement.setString(9, panno);
            preparedStatement.setObject(10, profession);
            preparedStatement.setObject(11, "requested");
            preparedStatement.setObject(12,amount );

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
   public void miniStatement() {
        System.out.println("Enter Your Account Number");
        BigInteger accno = s.nextBigInteger();
        String sql = "SELECT * from transactions WHERE accno = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, accno);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Amount = " + resultSet.getLong("amount") + " " +resultSet.getString("transaction_type")+" "+resultSet.getLong("balance"));
            }
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION " + e.getMessage());
        }
    }
   public void registrationNetBanking(){
            System.out.println("Enter Your Name");
            String name = s.nextLine();
            System.out.println("Enter Your Account Number");
            BigInteger acno = s.nextBigInteger();
            System.out.println("Enter Your Customer Id");
            BigInteger customerid = s.nextBigInteger();
            System.out.println("Enter Your Phone Number");
            BigInteger phone = s.nextBigInteger();
            System.out.println("Enter Your E-Mail Address");
            String email = s.next();
            System.out.println("Enter Pan Number");
            String panno = s.next();
            System.out.println("Enter Adhaar Number");
            BigInteger adhaar = s.nextBigInteger();
            System.out.println("Enter Debit Card Number");
            BigInteger debitcardno = s.nextBigInteger();

            String sql = "INSERT INTO netbankingusers (name, acno, customerid, phone, email, panno, adhaar, debitcardno) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection connection = DriverManager.getConnection(url);
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setObject(2, acno);
                preparedStatement.setObject(3, customerid);
                preparedStatement.setObject(4, phone);
                preparedStatement.setString(5, email);
                preparedStatement.setString(6, panno);
                preparedStatement.setObject(7, adhaar);
                preparedStatement.setObject(8, debitcardno);

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
   public void accStatusCheck()
   {
            System.out.println("Enter Your Customer Id");
            int customerid = s.nextInt();
            System.out.println("Enter Your Phone");
            BigInteger phone = s.nextBigInteger();
            String sql = "Select acstatus FROM customers WHERE customerid = ?  AND  phone = ? ";
            try (Connection connection = DriverManager.getConnection(url);
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, customerid);
                preparedStatement.setString(2, String.valueOf(phone));
                ResultSet resultSet = preparedStatement.executeQuery();

                // Process the resultSet as needed
                while (resultSet.next()) {
                    String acstatus = resultSet.getString("acstatus");
                    System.out.println(acstatus);
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }

        }
  public static long generateRandom10DigitNumber() {
        // Generating a random 10-digit number
        long lowerBound = 1000000000L; // 10 digits
        long upperBound = 9999999999L; // 10 digits
        return lowerBound + (long) (Math.random() * (upperBound - lowerBound + 1));
    }
 private long fetchBalance(BigInteger accno) throws SQLException {
        String fetchBalanceSql = "SELECT amount FROM customers WHERE accno = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement fetchStatement = connection.prepareStatement(fetchBalanceSql)) {

            fetchStatement.setObject(1, accno);

            try (ResultSet resultSet = fetchStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("amount");
                } else {
                    throw new SQLException("Customer not found while fetching balance.");
                }
            }
        }
    }
}
