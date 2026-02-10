import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame {
    // windows and buttons 
    private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JButton loginButton = new JButton("Login");
    private JButton cancelButton = new JButton("Cancel");

    private static final String CORRECT_USERNAME = "Ayush12";
    private static final String CORRECT_PASSWORD = "12345";

    public Login() {
        setTitle("Login Page");
        setSize(400, 250);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        setLocationRelativeTo(null); // for centering the window in middle 

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(40, 20, 100, 30);
        usernameLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        add(usernameLabel);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(40, 70, 100, 30);
        passwordLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        add(passwordLabel);

        usernameField.setBounds(150, 20, 150, 30);
        usernameField.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        usernameField.setBackground(new Color(255, 179, 0));
        add(usernameField);

        passwordField.setBounds(150, 70, 150, 30);
        passwordField.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        passwordField.setBackground(Color.ORANGE);
        add(passwordField);

        loginButton.setBounds(50, 140, 100, 40);
        loginButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
        add(loginButton);

        cancelButton.setBounds(200, 140, 100, 40);
        cancelButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
        add(cancelButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        setVisible(true);
    }

    private void handleLogin() {
        try {
            String enteredUsername = usernameField.getText().trim();
            String enteredPassword = new String(passwordField.getPassword()).trim();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                throw new IllegalArgumentException("Please fill in both fields.");
            }

            if (enteredUsername.equals(CORRECT_USERNAME) && enteredPassword.equals(CORRECT_PASSWORD)) {
                JOptionPane.showMessageDialog(this, "Login successful!"); 
                dispose(); 
                new HomeScreen();  
            } else {
                throw new Exception("Incorrect username or password. Please try again.");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please check your input.", "Input Error", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Login failed. Try again.");
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}