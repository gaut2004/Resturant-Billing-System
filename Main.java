package ResturantApp;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Display the login panel (either Employee or Admin based on user role)
        SwingUtilities.invokeLater(() -> new LoginPanel().setVisible(true));
    }
}

