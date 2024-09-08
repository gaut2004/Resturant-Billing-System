package ResturantApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EmployeePanel extends JFrame {
    private JTextField customerNameField;
    private JTextField customerPhoneField;
    private JTextField searchFoodField;
    private JTable billTable;
    private JLabel totalAmountLabel;
    private DefaultTableModel tableModel;
    private double totalAmount = 0.0;

    public EmployeePanel(String userId) {
        setTitle("Restaurant Billing System - Employee Panel");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // UI Components
        JLabel nameLabel = new JLabel("Customer Name:");
        customerNameField = new JTextField(20);
        JLabel phoneLabel = new JLabel("Phone Number:");
        customerPhoneField = new JTextField(15);
        JLabel searchLabel = new JLabel("Search Food:");
        searchFoodField = new JTextField(20);
        JButton addItemButton = new JButton("Add Item");
        tableModel = new DefaultTableModel(new Object[]{"Food Item", "Price"}, 0);
        billTable = new JTable(tableModel);
        totalAmountLabel = new JLabel("Total: ₹0.00");
        JButton printBillButton = new JButton("Print Bill");

        // Layout setup
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(nameLabel);
        panel.add(customerNameField);
        panel.add(phoneLabel);
        panel.add(customerPhoneField);
        panel.add(searchLabel);
        panel.add(searchFoodField);
        panel.add(addItemButton);
        panel.add(new JScrollPane(billTable));
        panel.add(totalAmountLabel);
        panel.add(printBillButton);
        add(panel);

        // Add item to bill
        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String foodName = searchFoodField.getText();
                addFoodItemToBill(foodName);
            }
        });

        // Print bill action
        printBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printBill();
            }
        });
    }

    private void addFoodItemToBill(String foodName) {
        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "SELECT * FROM Food_Items WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, foodName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                tableModel.addRow(new Object[]{name, price});
                totalAmount += price;
                totalAmountLabel.setText("Total: ₹" + String.format("%.2f", totalAmount));
            } else {
                JOptionPane.showMessageDialog(this, "Food item not found!");
            }

            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching for food item.");
        }
    }

    private void printBill() {
        String customerName = customerNameField.getText();
        String customerPhone = customerPhoneField.getText();

        // Example of saving the bill to the database
        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "INSERT INTO Bills (customer_name, customer_phone, total_amount) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, customerName);
            stmt.setString(2, customerPhone);
            stmt.setDouble(3, totalAmount);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int billId = 0;
            if (rs.next()) {
                billId = rs.getInt(1);
            }

            // Save bill items
            sql = "INSERT INTO Bill_Items (bill_id, food_item, price) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String foodItem = (String) tableModel.getValueAt(i, 0);
                double price = (Double) tableModel.getValueAt(i, 1);
                stmt.setInt(1, billId);
                stmt.setString(2, foodItem);
                stmt.setDouble(3, price);
                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Bill printed successfully!");

            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving bill details.");
        }

        // Reset the UI for the next customer
        customerNameField.setText("");
        customerPhoneField.setText("");
        searchFoodField.setText("");
        tableModel.setRowCount(0);
        totalAmount = 0.0;
        totalAmountLabel.setText("Total: ₹0.00");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeePanel("testUser").setVisible(true));
    }
}
