import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Department extends JFrame implements Displayable {
    private JTable departmentTable;
    private DefaultTableModel tableModel;
    private JButton backButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_Management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public Department() {
        setTitle("Hospital Departments");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Dept ID");
        tableModel.addColumn("Department Name");
        tableModel.addColumn("Head of Department");
        tableModel.addColumn("Contact");
        tableModel.addColumn("Location");
        tableModel.addColumn("Doctors");
        tableModel.addColumn("Nurses");

        departmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(departmentTable);
        add(scrollPane, BorderLayout.CENTER);

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
        String query = "SELECT * FROM department"; 
    
        try (
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery()
        ) {
    
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("dept_id"),
                    rs.getString("dept_name"),
                    rs.getString("head_of_department"),
                    rs.getString("contact_number"),
                    rs.getString("location"),
                    rs.getInt("no_of_doctors"),
                    rs.getInt("no_of_nurses")
                });
            }
    
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new Department();
    }
}
