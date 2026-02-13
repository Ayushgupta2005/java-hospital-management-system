import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AddPatient extends JFrame {

    private JComboBox<String> idTypeComboBox = new JComboBox<>(new String[]{"Aadhar Card", "Voter ID", "PAN ID"});
    private JTextField idNumberField = new JTextField();
    private JTextField nameField = new JTextField();
    private JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
    private JTextField diseaseField = new JTextField();
    private JLabel timeLabel = new JLabel(getCurrentTime());
    private JLabel dateLabel = new JLabel(getCurrentDate());
    private JTextField depositField = new JTextField();
    private JComboBox<String> roomComboBox = new JComboBox<>();
    private JButton submitButton = new JButton("Submit");
    private JButton cancelButton = new JButton("Cancel");

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_Management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    
    public AddPatient() {
        setTitle("Add New Patient");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("ID Type:"));  //Left 
        formPanel.add(idTypeComboBox);      // Right
        formPanel.add(new JLabel("ID Number:")); 
        formPanel.add(idNumberField);
        formPanel.add(new JLabel("Name:")); 
        formPanel.add(nameField);
        formPanel.add(new JLabel("Gender:")); 
        formPanel.add(genderComboBox);
        formPanel.add(new JLabel("Disease:")); 
        formPanel.add(diseaseField);
        formPanel.add(new JLabel("Time:")); 
        formPanel.add(timeLabel);
        formPanel.add(new JLabel("Admission Date:")); 
        formPanel.add(dateLabel);
        formPanel.add(new JLabel("Deposit Amount:")); 
        formPanel.add(depositField);
        formPanel.add(new JLabel("Select Room:")); 
        formPanel.add(roomComboBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton); 
        buttonPanel.add(cancelButton);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadAvailableRooms();
        setVisible(true);
    }

    private static String getCurrentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    private static String getCurrentDate() {
        return LocalDate.now().toString();
    }

    private void handleSubmit() {
        String idType = (String) idTypeComboBox.getSelectedItem();
        String idNumber = idNumberField.getText().trim();
        String name = nameField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();
        String disease = diseaseField.getText().trim();
        String deposit = depositField.getText().trim();
        String time = timeLabel.getText();
        String date = dateLabel.getText();
        String selectedRoom = (String) roomComboBox.getSelectedItem();

        if (idNumber.isEmpty() || name.isEmpty() || disease.isEmpty() || deposit.isEmpty() || selectedRoom == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (isRoomOccupied(con, selectedRoom)) {
                JOptionPane.showMessageDialog(this, "This room is already assigned!", "Room Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sql = "INSERT INTO patient_info (ID, Number, Name, Gender, Patient_Disease, Room_Number, Time, Deposit, admission_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, idType);
                ps.setString(2, idNumber);
                ps.setString(3, name);
                ps.setString(4, gender);
                ps.setString(5, disease);
                ps.setString(6, selectedRoom);
                ps.setString(7, time);
                ps.setString(8, deposit);
                ps.setString(9, date);
                ps.executeUpdate();
            }

            updateRoomStatus(con, selectedRoom);
            JOptionPane.showMessageDialog(this, "Patient Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAvailableRooms() {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT room_no FROM Room WHERE Availablility = 'Available'")) {

            boolean hasRooms = false;
            while (rs.next()) {
                roomComboBox.addItem(rs.getString("room_no"));
                hasRooms = true;
            }

            if (!hasRooms) {
                roomComboBox.addItem("No Available Rooms");
                roomComboBox.setEnabled(false);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isRoomOccupied(Connection con, String roomNumber) throws SQLException {
        String sql = "SELECT Availablility FROM Room WHERE room_no = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, roomNumber);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getString("Availablility").equalsIgnoreCase("Occupied");
            }
        }
    }

    private void updateRoomStatus(Connection con, String roomNumber) throws SQLException {
        String sql = "UPDATE Room SET Availablility = 'Occupied' WHERE room_no = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, roomNumber);
            pst.executeUpdate();
        }
    }

    public static void main(String[] args) {
        new AddPatient();
    }
}