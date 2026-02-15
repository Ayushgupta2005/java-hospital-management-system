import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SearchRoom extends JFrame implements Displayable {
    private JComboBox<String> statusComboBox;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JButton searchButton, backButton;


    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_Management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public SearchRoom() {
        setTitle("Search Room Availability");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

       
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("Room Status:"));

        statusComboBox = new JComboBox<>(new String[]{"Available", "Occupied"});
        panel.add(statusComboBox);

        searchButton = new JButton("Search");
        panel.add(searchButton);

        add(panel, BorderLayout.NORTH);

        // Table to display room details
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Room No");
        tableModel.addColumn("Availability");
        tableModel.addColumn("Price");
        tableModel.addColumn("Room Type");

        roomTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(roomTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        backButton = new JButton("Back to Home");
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayData();
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
    public void displayData() {
        String selectedStatus = (String) statusComboBox.getSelectedItem();
        if (selectedStatus == null) return;

        tableModel.setRowCount(0);

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement("SELECT * FROM Room WHERE Availablility = ?")) {  
            pst.setString(1, selectedStatus);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getString("room_no"),
                            rs.getString("Availablility"), 
                            rs.getString("Price"),
                            rs.getString("Room_Type")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new SearchRoom();
    }
}
