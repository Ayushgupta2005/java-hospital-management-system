import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PatientInfo extends JFrame implements Displayable {
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JButton backButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_Management";
    private static final String DB_USER = "root";  
    private static final String DB_PASSWORD = "";  

    public PatientInfo() {
        setTitle("Patient Information");
        setSize(1000, 400);  
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID Type");
        tableModel.addColumn("ID Number");
        tableModel.addColumn("Name");
        tableModel.addColumn("Gender");
        tableModel.addColumn("Disease");
        tableModel.addColumn("Room Number");
        tableModel.addColumn("Time Admitted");
        tableModel.addColumn("Deposit");
        tableModel.addColumn("Admission Date");  

        patientTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("Back to HomeScreen");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HomeScreen(); // Open HomeScreen
                dispose(); // Close PatientInfo window
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        displayData(); 

        setVisible(true);
    }

    public void displayData() {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM patient_info")) {  

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("ID"),
                    rs.getString("Number"),
                    rs.getString("Name"),
                    rs.getString("Gender"),
                    rs.getString("Patient_Disease"),
                    rs.getString("Room_Number"),
                    rs.getString("Time"),
                    rs.getString("Deposit"),
                    rs.getDate("admission_date")  
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new PatientInfo();
    }
}
