import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class PatientDischarge extends JFrame {
    private JComboBox<String> patientIdComboBox;
    private JLabel nameLabel = new JLabel("N/A");
    private JLabel roomLabel = new JLabel("N/A");
    private JButton checkButton = new JButton("Check");
    private JButton dischargeButton = new JButton("Discharge");
    private JButton backButton = new JButton("Back to HomeScreen");

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_Management";
    private static final String DB_USER = "root";  
    private static final String DB_PASSWORD = "";  

    public PatientDischarge() {
        setTitle("CHECK-OUT");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Select Patient ID:"));
        patientIdComboBox = new JComboBox<>();
        loadPatientIDs();
        add(patientIdComboBox);

        add(new JLabel("Patient Name:"));
        add(nameLabel);

        add(new JLabel("Room Number:"));
        add(roomLabel);

        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadPatientDetails();
            }
        });
        add(checkButton);

        dischargeButton.setEnabled(false); 
        dischargeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dischargePatient();
            }
        });
        add(dischargeButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HomeScreen();
                dispose();
            }
        });
        add(backButton);

        setVisible(true);
    }

    private void loadPatientIDs() {
        String query = "SELECT Number FROM patient_info";
    
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
    
            ArrayList<String> ids = new ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getString("Number"));
            }
    
            if (ids.isEmpty()) {
                patientIdComboBox.addItem("No Patients Available");
                patientIdComboBox.setEnabled(false);
            } else {
                for (String id : ids) {
                    patientIdComboBox.addItem(id);
                }
            }
    
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPatientDetails() {
        String selectedID = (String) patientIdComboBox.getSelectedItem();
        if (selectedID == null || selectedID.equals("No Patients Available")) {
            return;
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement("SELECT Name, Room_Number FROM patient_info WHERE Number = ?")) {
            pst.setString(1, selectedID);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    nameLabel.setText(rs.getString("Name"));
                    roomLabel.setText(rs.getString("Room_Number"));
                    dischargeButton.setEnabled(true);
                } else {
                    nameLabel.setText("N/A");
                    roomLabel.setText("N/A");
                    dischargeButton.setEnabled(false);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dischargePatient() {
        String selectedID = (String) patientIdComboBox.getSelectedItem();
        String roomNumber = roomLabel.getText();

        if (selectedID == null || roomNumber.equals("N/A")) {
            JOptionPane.showMessageDialog(this, "Invalid selection.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            try (PreparedStatement pst = con.prepareStatement("DELETE FROM patient_info WHERE Number = ?")) {
                pst.setString(1, selectedID);
                pst.executeUpdate();
            }

            try (PreparedStatement pst = con.prepareStatement("UPDATE Room SET Availablility = 'Available' WHERE room_no = ?")) {
                pst.setString(1, roomNumber);
                pst.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Patient Discharged Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new PatientDischarge();
    }
}