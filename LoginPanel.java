package ResturantApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;

    public LoginPanel() {
        setTitle("Restaurant Billing System - Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create UI components
        JLabel userIdLabel = new JLabel("User ID:");
        userIdField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        // Layout
        setLayout(new GridLayout(3, 2));
        add(userIdLabel);
        add(userIdField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel());  // Empty cell
        add(loginButton);

        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText();
                String password = new String(passwordField.getPassword());
                // Authentication logic
                if (DatabaseHelper.authenticateUser(userId, password)) {
                    if (DatabaseHelper.isAdmin(userId)) {
                        new AdminPanel(userId).setVisible(true);
                    } else {
                        new EmployeePanel(userId).setVisible(true);
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

