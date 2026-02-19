
import java.io.*;

public class AllEmployeeInfo implements Displayable {
    private static final String FILE_PATH = "/Users/ayushgupta/Desktop/Hospital Management System_Text based /data/employees.txt";
    private Employee[] employees;  
    private int employeeCount = 0;


    public AllEmployeeInfo() {
        employees = new Employee[50]; 
        loadEmployeesFromFile();  
    }

    public void displayData() {
        if (employeeCount == 0) {
            System.out.println(" No employee records found.");
            return;
        }

        System.out.println("\n===== Employee Records =====");
        System.out.printf("%-10s %-25s %-15s %-10s %-10s %-20s %-15s %-15s %-15s%n",
                "Emp ID", "Name", "Designation", "Dept ID", "Salary", "Email", "Specialization", "Shift", "Contact");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");

        // Display each employee
        for (int i = 0; i < employeeCount; i++) {
            employees[i].displayInfo();  
        }
    }

    private void loadEmployeesFromFile() {
        File file = new File(FILE_PATH);

        if (!file.exists() || file.length() == 0) {
            System.out.println(" No employees found (File missing or empty).");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null && employeeCount < employees.length) {
                String[] details = line.split(",");  
                if (details.length == 9) {  
                    String empId = details[0].trim();
                    String name = details[1].trim();
                    String designation = details[2].trim();
                    String deptId = details[3].trim();
                    double salary = Double.parseDouble(details[4].trim());
                    String email = details[5].trim();
                    String specialization = details[6].trim();
                    String shift = details[7].trim();
                    String contact = details[8].trim();

                    if (designation.equalsIgnoreCase("Doctor")) {
                        employees[employeeCount++] = new Doctor(empId, name, deptId, salary, email, specialization, shift, contact); //array(employees) of object 
                    } else if (designation.equalsIgnoreCase("Nurse")) {
                        employees[employeeCount++] = new Nurse(empId, name, deptId, salary, email, specialization, shift, contact);
                    } else {
                        employees[employeeCount++] = new AdminStaff(empId, name, deptId, salary, email, specialization, shift, contact);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println(" Error loading employee data: " + e.getMessage());
        }
    }

    // Main method to run the program independently
    public static void main(String[] args) {
        AllEmployeeInfo allEmployees = new AllEmployeeInfo();
        allEmployees.displayData();  // Display employee details
    }
}