import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomeScreen extends JFrame {
    public HomeScreen() {
        setTitle("Hospital Management System - HomeScreen");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // to Center the window
        setLayout(new GridLayout(3, 3, 10, 10)); 


        JButton addPatientButton = new JButton("Add New Patients");
        JButton roomButton = new JButton("Room");
        JButton departmentButton = new JButton("Department");
        JButton employeeButton = new JButton("All Employee Info");
        JButton patientInfoButton = new JButton("Patient Info");
        JButton dischargeButton = new JButton("Patient Discharge");
        JButton updateDetailsButton = new JButton("Update Patient Details");
        JButton searchRoomButton = new JButton("Search Room");

        JButton[] buttons = {addPatientButton, roomButton, departmentButton, employeeButton, patientInfoButton,
                dischargeButton, updateDetailsButton, searchRoomButton};

        for (JButton button : buttons) {
            button.setFont(new Font("Times New Roman", Font.BOLD, 14));
            add(button);
        }
// after clicking on button the respective constructor is called
        addPatientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AddPatient(); 
            }
        });

        roomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Room(); 
            }
        });

        departmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Department();  
            }
        });

        employeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Employee();  
            }
        });

        patientInfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PatientInfo();  
            }
        });

        dischargeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PatientDischarge();  
            }
        });

        updateDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UpdatePatientDetail();  
            }
        });

        searchRoomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SearchRoom();  
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new HomeScreen();
    }
}
