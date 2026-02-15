import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Room extends JFrame implements Displayable {
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JButton backButton;

    // Database Connection Details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_Management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public Room() {
        setTitle("Room Information");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        tableModel = new DefaultTableModel();
        roomTable = new JTable(tableModel);
        tableModel.addColumn("Room No");
        tableModel.addColumn("Availability");
        tableModel.addColumn("Price");
        tableModel.addColumn("Room Type");

        add(roomTable, BorderLayout.CENTER);

        // Back Button
        backButton = new JButton("Back to HomeScreen");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HomeScreen();
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        displayData(); // Calls the interface method

        setVisible(true);
    }

    public void displayData() {
        String query = "SELECT * FROM Room"; 
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = con.prepareStatement(query); 
             ResultSet rs = ps.executeQuery()) {
    
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("room_no"),
                    rs.getString("Availablility"),
                    rs.getString("Price"),
                    rs.getString("Room_Type")
                });
            }
    
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        new Room();
    }
}
