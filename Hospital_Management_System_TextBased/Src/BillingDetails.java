import java.io.*;
import java.util.Scanner;

public class BillingDetails {
    private static final String PATIENT_FILE = "/Users/ayushgupta/Desktop/Hospital Management System_Text based /data/patients.txt";

    public static void updateBillingDetails(Scanner scanner) {
        String[] patients = new String[100]; // Array to store patient records
        int patientCount = 0;

        System.out.println("\n===== Patient Billing Details =====");
        System.out.printf("%-10s %-20s %-5s %-10s %-20s %-10s %-10s %-15s %-15s %-15s %-15s%n",
                "ID", "Name", "Age", "Gender", "Disease", "Deposit", "Room No", "Date", "Time", "Total Bill", "Remaining Amount");
        System.out.println("------------------------------------------------------------------------------------------------------------------------");

        // Read patients from file
        try (BufferedReader reader = new BufferedReader(new FileReader(PATIENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null && patientCount < patients.length) {
                String[] details = line.split(",", -1);  //details array 

                if (details.length == 11) { // checking if there are 11 records in file if not throw an error
                    System.out.printf("%-10s %-20s %-5s %-10s %-20s %-10s %-10s %-15s %-15s %-15s %-15s%n",
                            details[0], details[1], details[2], details[3], details[4], details[5], details[6],
                            details[7], details[8], details[9], details[10]);
                    patients[patientCount++] = line; // Store valid records
                } else {
                    System.out.println("invalid record: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println(" Error reading patient data: " + e.getMessage());
            return;
        }

        // Ask for Patient ID to update billing details
        System.out.print("\nEnter Patient ID to update billing details: ");
        String patientId = scanner.nextLine().trim();
        boolean patientFound = false;

        // Loop through the patient array and update billing details
        for (int i = 0; i < patientCount; i++) {
            String[] details = patients[i].split(",");

            if (details[0].equals(patientId)) {
                patientFound = true;
                System.out.println("\n Updating billing details for: " + details[1]);

                double depositAmount = Double.parseDouble(details[5]);
                double totalBill = Double.parseDouble(details[9]);
                double remainingAmount = Double.parseDouble(details[10]);

                while (true) {
                    System.out.println("\nChoose an option:");
                    System.out.println("1. Update Total Bill");
                    System.out.println("2. Pay Remaining Amount");
                    System.out.println("3. Exit");
                    System.out.print("Enter your choice: ");
                    String choice = scanner.nextLine().trim();

                    if (choice.equals("1")) {
                        System.out.print("Enter new Total Bill Amount: ");
                        double newTotalBill = Double.parseDouble(scanner.nextLine().trim());

                        if (totalBill == 0) { //if billing is first time subtract the deposit amount
                            remainingAmount = newTotalBill - depositAmount;
                        } else { // Just update the total bill without subtracting deposit again
                            remainingAmount += (newTotalBill - totalBill);
                        }

                        totalBill = newTotalBill;
                        System.out.println(" Updated Remaining Amount: " + remainingAmount);

                    } else if (choice.equals("2")) {
                        System.out.print("Enter amount to pay: ");
                        double payment = Double.parseDouble(scanner.nextLine().trim());

                        if (payment > remainingAmount) {
                            System.out.println(" Payment exceeds remaining amount! Enter a valid amount.");
                            continue;
                        }

                        remainingAmount -= payment;
                        System.out.println(" Payment successful! New Remaining Amount: " + remainingAmount);

                    } else if (choice.equals("3")) {
                        break;
                    } else {
                        System.out.println(" Invalid choice. Please enter 1, 2, or 3.");
                        continue;
                    }

                    // Update details in the array
                    details[9] = String.valueOf(totalBill);
                    details[10] = String.valueOf(remainingAmount);
                    patients[i] = String.join(",", details);
                }
                break;
            }
        }

        if (!patientFound) {
            System.out.println(" No patient found with ID: " + patientId);
            return;
        }

        // Save updated records back to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATIENT_FILE))) {
            for (int i = 0; i < patientCount; i++) {
                writer.write(patients[i]);
                writer.newLine();
            }
            System.out.println(" Billing details updated successfully!");
        } catch (IOException e) {
            System.out.println("Error updating patient data: " + e.getMessage());
        }
    }
}