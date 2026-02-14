import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Employee extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton backButton;


    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_Management";
    private static final String DB_USER = "root";  
    private static final String DB_PASSWORD = "";  

    public Employee() {
        setTitle("Employee Information");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());


        tableModel = new DefaultTableModel();
        tableModel.addColumn("Emp ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Designation");
        tableModel.addColumn("Dept ID");
        tableModel.addColumn("Contact");
        tableModel.addColumn("Salary");

        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("Back to HomeScreen");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HomeScreen(); 
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadEmployeeData(); 

        setVisible(true);
    }

    private void loadEmployeeData() {
            String query = "SELECT emp_id, name, designation, department_id, contact_number, salary FROM employee";
        
            try (
                Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()
            ) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("emp_id"),
                        rs.getString("name"),
                        rs.getString("designation"),
                        rs.getString("department_id"),
                        rs.getString("contact_number"),
                        rs.getDouble("salary")
                    });
                }
        
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database Connection Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    public static void main(String[] args) {
        new Employee();
    }
}
