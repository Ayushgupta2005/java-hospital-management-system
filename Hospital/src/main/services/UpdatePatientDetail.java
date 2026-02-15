import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdatePatientDetail extends JFrame {
    private JComboBox<String> patientNameComboBox;
    private JLabel timeLabel, dateLabel, amtPaidLabel, pendingAmtLabel;
    private JTextField totalAmountField, additionalDepositField;
    private JButton checkButton, updateTotalButton, makeDepositButton, backButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_Management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public UpdatePatientDetail() {
        setTitle("Update Patient Details");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        formPanel.add(new JLabel("Select Patient:"));
        patientNameComboBox = new JComboBox<>();
        loadPatientNames();
        formPanel.add(patientNameComboBox);

 formPanel.add(new JLabel("Check-in Time:"));
 timeLabel = new JLabel("N/A");
 formPanel.add(timeLabel);

    formPanel.add(new JLabel("Check-in Date:"));
    dateLabel = new JLabel("N/A");
    formPanel.add(dateLabel);

        formPanel.add(new JLabel("Amount Paid:"));
        amtPaidLabel = new JLabel("N/A");
        formPanel.add(amtPaidLabel);

        formPanel.add(new JLabel("Enter Total Bill Amount:"));
        totalAmountField = new JTextField();
        formPanel.add(totalAmountField);

        formPanel.add(new JLabel("Remaining Amount:"));
        pendingAmtLabel = new JLabel("N/A");
        formPanel.add(pendingAmtLabel);

        formPanel.add(new JLabel("Enter Additional Deposit:"));
        additionalDepositField = new JTextField();
        additionalDepositField.setEnabled(false);
        formPanel.add(additionalDepositField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        checkButton = new JButton("Check");
        updateTotalButton = new JButton("Update Total Bill");
        makeDepositButton = new JButton("Make Additional Deposit");
        backButton = new JButton("Back to HomeScreen");

        buttonPanel.add(checkButton);
        buttonPanel.add(updateTotalButton);
        buttonPanel.add(makeDepositButton);
        buttonPanel.add(backButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadPatientDetails();
            }
        });

        updateTotalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTotalBill();
            }
        });

        makeDepositButton.setEnabled(false);
        makeDepositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeAdditionalDeposit();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HomeScreen();
                dispose();
            }
        });

        setVisible(true);
    }

    private void loadPatientNames() {
        String query = "SELECT Name FROM patient_info";
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
    
            while (rs.next()) {
                patientNameComboBox.addItem(rs.getString("Name"));
            }
    
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPatientDetails() {
        String selectedName = (String) patientNameComboBox.getSelectedItem();
        if (selectedName == null) return;

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement(
                     "SELECT Time, admission_date, Deposit, Total_Bill, Pending_Amount FROM patient_info WHERE Name = ?")) {
            pst.setString(1, selectedName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    timeLabel.setText(rs.getString("Time"));
                    dateLabel.setText(rs.getString("admission_date"));
                    amtPaidLabel.setText(rs.getString("Deposit"));
                    totalAmountField.setText(rs.getString("Total_Bill"));
                    pendingAmtLabel.setText(rs.getString("Pending_Amount"));

                    // Enable deposit field and button after total bill is set
                    additionalDepositField.setEnabled(true);
                    makeDepositButton.setEnabled(true);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTotalBill() {
        String selectedName = (String) patientNameComboBox.getSelectedItem();
        if (selectedName == null || totalAmountField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a valid total bill amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement(
                     "UPDATE patient_info SET Total_Bill = ?, Pending_Amount = Total_Bill - Deposit WHERE Name = ?")) {

            double totalAmount = Double.parseDouble(totalAmountField.getText().trim());
            pst.setDouble(1, totalAmount);
            pst.setString(2, selectedName);
            pst.executeUpdate();

            // Refresh display
            loadPatientDetails();
            JOptionPane.showMessageDialog(this, "Total Bill & Pending Amount Updated!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void makeAdditionalDeposit() {
        String selectedName = (String) patientNameComboBox.getSelectedItem();
        if (selectedName == null || additionalDepositField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a valid deposit amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement updateDeposit = con.prepareStatement(
                     "UPDATE patient_info SET Deposit = Deposit + ?, Pending_Amount = Total_Bill - Deposit WHERE Name = ?")) {

            double additionalDeposit = Double.parseDouble(additionalDepositField.getText().trim());
            updateDeposit.setDouble(1, additionalDeposit);
            updateDeposit.setString(2, selectedName);
            updateDeposit.executeUpdate();

            // Refresh display
            loadPatientDetails();
            additionalDepositField.setText("");
            JOptionPane.showMessageDialog(this, "Deposit Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new UpdatePatientDetail();
    }
}
