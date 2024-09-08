package ResturantApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/RestaurantBillingSystem";
    private static final String USER = "root";
    private static final String PASSWORD = "Gautam@2004";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean authenticateUser(String userId, String password) {
        try (Connection connection = getConnection()) {
            // Ensure the SQL query is correct
            String query = "SELECT * FROM Employees WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            // Set parameters in the correct order
            statement.setString(1, userId);
            statement.setString(2, password);

            // Execute the query and check if any result is returned
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean isAdmin(String userId) {
        try (Connection connection = getConnection()) {
            String query = "SELECT is_admin FROM Employees WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean("is_admin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Add a new employee to the database
    public static boolean addEmployee(String name, String phoneNumber, String gender, String aadharNumber, String userId, String password) {
        String query = "INSERT INTO Employees (name, phoneNumber, gender, aadharNumber, username, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, phoneNumber);
            statement.setString(3, gender);
            statement.setString(4, aadharNumber);
            statement.setString(5, userId);
            statement.setString(6, password);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Remove an employee from the database
    public static boolean removeEmployee(int empId) {
        try (Connection connection = getConnection()) {
            String query = "DELETE FROM Employees WHERE emp_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, empId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add a new food item to the database
    public static boolean addFoodItem(String name, double price) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO Food_Items (name, price) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setDouble(2, price);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Remove a food item from the database
    public static boolean removeFoodItem(int itemId) {
        try (Connection connection = getConnection()) {
            String query = "DELETE FROM Food_Items WHERE item_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, itemId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Record a bill in the database
    public static int recordBill(String customerName, String phoneNumber, double totalAmount, String paymentMethod, int empId) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO Bills (customer_name, phone_number, total_amount, payment_method, emp_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, customerName);
            statement.setString(2, phoneNumber);
            statement.setDouble(3, totalAmount);
            statement.setString(4, paymentMethod);
            statement.setInt(5, empId);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);  // Return the generated bill_id
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Record a bill item in the database
    public static boolean recordBillItem(int billId, int itemId, int quantity, double itemPrice) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO Bill_Items (bill_id, item_id, quantity, item_price) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, billId);
            statement.setInt(2, itemId);
            statement.setInt(3, quantity);
            statement.setDouble(4, itemPrice);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get list of all food items
    public static List<String> getFoodItems() {
        List<String> items = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM Food_Items";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                items.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Record employee login time (attendance)
    public static boolean recordLoginTime(int empId) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO Employee_Attendance (emp_id, login_time) VALUES (?, NOW())";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, empId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Record employee logout time (attendance)
    public static boolean recordLogoutTime(int empId) {
        try (Connection connection = getConnection()) {
            String query = "UPDATE Employee_Attendance SET logout_time = NOW() WHERE emp_id = ? AND logout_time IS NULL";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, empId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // View employee attendance
    public static List<String> getEmployeeAttendance(int empId) {
        List<String> attendanceRecords = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String query = "SELECT login_time, logout_time FROM Employee_Attendance WHERE emp_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, empId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String record = "Login: " + resultSet.getTimestamp("login_time") + ", Logout: " + resultSet.getTimestamp("logout_time");
                attendanceRecords.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceRecords;
    }

    // Generate sales data for graphing
    public static List<Double> getSalesData(String period) {
        List<Double> salesData = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String query = "";
            if (period.equals("Daily")) {
                query = "SELECT total_sales FROM Sales WHERE date = CURDATE()";
            } else if (period.equals("Monthly")) {
                query = "SELECT SUM(total_sales) AS monthly_sales FROM Sales WHERE MONTH(date) = MONTH(CURDATE())";
            } else if (period.equals("Yearly")) {
                query = "SELECT SUM(total_sales) AS yearly_sales FROM Sales WHERE YEAR(date) = YEAR(CURDATE())";
            }

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                salesData.add(resultSet.getDouble(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesData;
    }
}


