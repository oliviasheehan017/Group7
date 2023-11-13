import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class LoginPanel extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private static User loggedInUser;
    private JButton submitButton; // New Submit button
    private JButton switchModeButton; // New button to switch between login and create account modes
    private boolean createAccountMode = false; // Flag to indicate whether the panel is in create account mode

    public LoginPanel() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        submitButton = new JButton("Submit");

        // Add action listener to the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (createAccountMode) {
                    if (createAccount()) {
                        switchToLoginMode();
                        JOptionPane.showMessageDialog(LoginPanel.this, "Account created successfully. You can now log in.");
                    } else {
                        JOptionPane.showMessageDialog(LoginPanel.this, "Failed to create account. Please try again.");
                    }
                } else {
                    if (authenticateUser()) {
                        dispose(); // Close the login window
                    } else {
                        JOptionPane.showMessageDialog(LoginPanel.this, "Invalid username or password");
                    }
                }
            }
        });



        // Button to switch between login and create account modes
        switchModeButton = new JButton("Create Account");
        switchModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (createAccountMode) {
                    switchToLoginMode();
                } else {
                    switchToCreateAccountMode();
                }
            }
        });

        setLayout(new GridLayout(5, 2));
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Empty label for spacing
        add(submitButton); // Add the submit button
        add(new JLabel()); // Empty label for spacing
        add(switchModeButton); // Add the switch mode button

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    private boolean authenticateUser() {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        // Perform authentication using the entered username and password
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/movies", "root", "root")) {
            String query = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    loggedInUser = new User(resultSet.getInt("UserID"), username);
                    return true; // Authentication successful
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Authentication failed
    }

    private boolean createAccount() {
        String newUsername = usernameField.getText();
        char[] newPasswordChars = passwordField.getPassword();
        String newPassword = new String(newPasswordChars);

        // Perform insertion into the Users table
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/movies", "root", "root")) {
            String insertQuery = "INSERT INTO Users (Username, Password) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, newUsername);
                preparedStatement.setString(2, newPassword);

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }



    private void switchToCreateAccountMode() {
        createAccountMode = true;
        submitButton.setText("Create Account");
        switchModeButton.setText("Back to Login");
    }

    
    private void switchToLoginMode() {
        createAccountMode = false;
        submitButton.setText("Submit");
        switchModeButton.setText("Create Account");
    }

    public static boolean isAuthenticated() {
        return loggedInUser != null;
    }
}


