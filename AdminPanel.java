package ResturantApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List; // Add this import statement

public class AdminPanel extends JFrame {

    public AdminPanel(String userId) {
        setTitle("Restaurant Billing System - Admin Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tabs for different admin functionalities
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Add Employee", new AddEmployeePanel());
        tabbedPane.addTab("Remove Employee", new RemoveEmployeePanel());
        tabbedPane.addTab("Employee Attendance", new EmployeeAttendancePanel());
        tabbedPane.addTab("Add Food Item", new AddFoodItemPanel());
        tabbedPane.addTab("Remove Food Item", new RemoveFoodItemPanel());
        tabbedPane.addTab("View Sales", new ViewSalesPanel());
        add(tabbedPane);
    }



    private class AddEmployeePanel extends JPanel {
        private JTextField nameField;
        private JTextField phoneField;
        private JTextField genderField;
        private JTextField adharField;
        private JTextField usernameField;
        private JTextField passwordField;

        public AddEmployeePanel() {
            setLayout(null);

            JLabel nameLabel = new JLabel("Name:");
            nameLabel.setBounds(10, 10, 80, 25);
            add(nameLabel);

            nameField = new JTextField(20);
            nameField.setBounds(100, 10, 160, 25);
            add(nameField);

            JLabel phoneLabel = new JLabel("Phone:");
            phoneLabel.setBounds(10, 40, 80, 25);
            add(phoneLabel);

            phoneField = new JTextField(20);
            phoneField.setBounds(100, 40, 160, 25);
            add(phoneField);

            JLabel genderLabel = new JLabel("Gender:");
            genderLabel.setBounds(10, 70, 80, 25);
            add(genderLabel);

            genderField = new JTextField(20);
            genderField.setBounds(100, 70, 160, 25);
            add(genderField);

            JLabel adharLabel = new JLabel("Aadhar No:");
            adharLabel.setBounds(10, 100, 80, 25);
            add(adharLabel);

            adharField = new JTextField(20);
            adharField.setBounds(100, 100, 160, 25);
            add(adharField);

            JLabel usernameLabel = new JLabel("Username:");
            usernameLabel.setBounds(10, 130, 80, 25);
            add(usernameLabel);

            usernameField = new JTextField(20);
            usernameField.setBounds(100, 130, 160, 25);
            add(usernameField);

            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setBounds(10, 160, 80, 25);
            add(passwordLabel);

            passwordField = new JTextField(20);
            passwordField.setBounds(100, 160, 160, 25);
            add(passwordField);

            JButton addButton = new JButton("Add Employee");
            addButton.setBounds(10, 200, 150, 25);
            add(addButton);

            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = nameField.getText();
                    String phone = phoneField.getText();
                    String gender = genderField.getText();
                    String adhar = adharField.getText();
                    String username = usernameField.getText();
                    String password = passwordField.getText();

                    try (Connection conn = DatabaseHelper.getConnection()) {
                        String sql = "INSERT INTO Employees (name, phone_number, gender, aadhaar_number, username, password) VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, name);
                        stmt.setString(2, phone);
                        stmt.setString(3, gender);
                        stmt.setString(4, adhar);
                        stmt.setString(5, username);
                        stmt.setString(6, password);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Employee added successfully!");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error adding employee.");
                    }
                }
            });
        }
    }

        private class RemoveEmployeePanel extends JPanel {
        private JTextField employeeIdField;

        public RemoveEmployeePanel() {
            setLayout(null);

            JLabel employeeIdLabel = new JLabel("Employee ID:");
            employeeIdLabel.setBounds(10, 10, 100, 25);
            add(employeeIdLabel);

            employeeIdField = new JTextField(20);
            employeeIdField.setBounds(120, 10, 160, 25);
            add(employeeIdField);

            JButton removeButton = new JButton("Remove Employee");
            removeButton.setBounds(10, 50, 150, 25);
            add(removeButton);

            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int employeeId = Integer.parseInt(employeeIdField.getText());

                    try (Connection conn = DatabaseHelper.getConnection()) {
                        String sql = "DELETE FROM Employees WHERE id = ?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, employeeId);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Employee removed successfully!");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error removing employee.");
                    }
                }
            });
        }
    }

    private class EmployeeAttendancePanel extends JPanel {
        public EmployeeAttendancePanel() {
            // UI Components for viewing employee attendance
        }
    }

    private class AddFoodItemPanel extends JPanel {
        private JTextField foodNameField;
        private JTextField priceField;

        public AddFoodItemPanel() {
            setLayout(null);

            JLabel foodNameLabel = new JLabel("Food Name:");
            foodNameLabel.setBounds(10, 10, 100, 25);
            add(foodNameLabel);

            foodNameField = new JTextField(20);
            foodNameField.setBounds(120, 10, 160, 25);
            add(foodNameField);

            JLabel priceLabel = new JLabel("Price:");
            priceLabel.setBounds(10, 40, 100, 25);
            add(priceLabel);

            priceField = new JTextField(20);
            priceField.setBounds(120, 40, 160, 25);
            add(priceField);

            JButton addButton = new JButton("Add Food Item");
            addButton.setBounds(10, 80, 150, 25);
            add(addButton);

            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String foodName = foodNameField.getText();
                    double price = Double.parseDouble(priceField.getText());

                    try (Connection conn = DatabaseHelper.getConnection()) {
                        String sql = "INSERT INTO Food_Items (name, price) VALUES (?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, foodName);
                        stmt.setDouble(2, price);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Food item added successfully!");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error adding food item.");
                    }
                }
            });
        }
    }

    private class RemoveFoodItemPanel extends JPanel {
        private JTextField foodIdField;

        public RemoveFoodItemPanel() {
            setLayout(null);

            JLabel foodIdLabel = new JLabel("Food ID:");
            foodIdLabel.setBounds(10, 10, 100, 25);
            add(foodIdLabel);

            foodIdField = new JTextField(20);
            foodIdField.setBounds(120, 10, 160, 25);
            add(foodIdField);

            JButton removeButton = new JButton("Remove Food Item");
            removeButton.setBounds(10, 50, 150, 25);
            add(removeButton);

            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int foodId = Integer.parseInt(foodIdField.getText());

                    try (Connection conn = DatabaseHelper.getConnection()) {
                        String sql = "DELETE FROM Food_Items WHERE id = ?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, foodId);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Food item removed successfully!");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error removing food item.");
                    }
                }
            });
        }
    }

    private class ViewSalesPanel extends JPanel {
        public ViewSalesPanel() {
            // Example: Assuming getSalesData() returns List<Double>
            List<Double> salesData = DatabaseHelper.getSalesData("daily");

            // Create a panel to display the sales data (e.g., using a chart)
            // For illustration, just printing the sales data in the console
            for (Double data : salesData) {
                System.out.println(data);
            }

            // Implement the logic to convert List<Double> to a format suitable for your UI
            // e.g., drawing a chart or displaying in a table
        }
    }
}
