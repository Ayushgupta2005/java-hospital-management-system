import java.util.Scanner;

public class MainMenu {
    public static void display() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n===== Hospital Management System =====");
            System.out.println("1. Add New Patient");
            System.out.println("2. View Patient Info");
            System.out.println("3. Departments");
            System.out.println("4. All Employee Info");
            System.out.println("5. Room Information");
            System.out.println("6. Discharge Patients");
            System.out.println("7. Update Patient Billing Details");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(" Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    AddPatient.addPatient(scanner);
                    break;
                case 2:
                    ViewPatients viewPatients = new ViewPatients();
                    viewPatients.displayData(); 
                    break;
                case 3:
                    Department department = new Department();
                    department.displayData();
                    break;
                case 4:
                    AllEmployeeInfo allEmployees = new AllEmployeeInfo();
                    allEmployees.displayData();
                    break;
                case 5:
                    Room room = new Room();
                    room.displayData();
                    break;
                case 6:
                    DischargePatient.dischargePatient(scanner);
                    break;
                case 7:
                    BillingDetails.updateBillingDetails(scanner);
                    break;
                case 8:
                    System.out.println(" Exiting the system. Thank You");
                    scanner.close();
                    return;
                default:
                    System.out.println(" Invalid choice! Please try again.");
            }
        }
    }
}