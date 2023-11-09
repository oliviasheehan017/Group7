import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    private static void createAndShowGUI() {
        JFrame jFrame = new JFrame("Login Please");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(null); // Set the layout manager to null
      
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 210, 90, 30);

        JTextField t1, t2;
        JLabel user, password;

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        
        t1 = new JTextField();
        t1.setBounds(100, 130, 90, 30);
        t2 = new JPasswordField(); // Use JPasswordField for password
        t2.setBounds(100, 160, 90, 30);

        userLabel.setBounds(20, 130, 80, 30);
        passLabel.setBounds(20, 160, 80, 30);

        jFrame.add(userLabel);
        jFrame.add(t1);
        jFrame.add(passLabel);
        jFrame.add(t2);
        jFrame.add(loginButton);

        jFrame.setSize(300, 600);
        jFrame.setLayout(null);
        jFrame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = t1.getText();
                String password = t2.getText(); // Use getText() for JPasswordField

                if (username.equals("software") && password.equals("engineering")) {
                    JOptionPane.showMessageDialog(jFrame, "Success");
                } else {
                    JOptionPane.showMessageDialog(jFrame, "Unsuccessful");
                }
            }
        });
    }

    public static void main(String[] args) {
        createAndShowGUI();
    }
}
