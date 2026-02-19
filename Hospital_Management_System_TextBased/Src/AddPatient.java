import java.io.*;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddPatient {
    private static final String PATIENT_FILE = "/Users/ayushgupta/Desktop/Hospital Management System_Text based /data/patients.txt";  
    private static final String ROOM_FILE = "/Users/ayushgupta/Desktop/Hospital Management System_Text based /data/rooms.txt";  

    public static void addPatient(Scanner scanner) {  
        System.out.println("\n===== Add New Patient =====");

        String[] checkInDateTime = getCurrentDateTime();  

        String totalBill = "0";
        String remainingAmount = "0";

        String id;
        while (true) {
            System.out.print("Enter Patient ID(Aadhar): ");
            id = scanner.nextLine().trim();
            if (!isPatientIdTaken(id)) {
                break;
            } else {
                System.out.println(" This Patient ID is already taken! Please enter a different ID.");
            }
        }

        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Age: ");
        String age = scanner.nextLine().trim();
        System.out.print("Enter Gender (Male/Female): ");
        String gender = scanner.nextLine().trim();
        System.out.print("Enter Disease: ");
        String disease = scanner.nextLine().trim();
        System.out.print("Enter Deposit Amount: ");
        String deposit = scanner.nextLine().trim();

        // Room selection (Checks if room is available)
        String roomNumber;
        while (true) {
            System.out.print("Enter Room Number: ");
            roomNumber = scanner.nextLine().trim();
            if (isRoomAvailable(roomNumber)) {
                updateRoomStatus(roomNumber, "Occupied");
                break;
            } else {
                System.out.println(" This room is already occupied. Choose another.");
            }
        }

        String patientData = id + "," + name + "," + age + "," + gender + "," + disease + "," + deposit + "," + roomNumber + "," + checkInDateTime[0] + "," + checkInDateTime[1] + "," + totalBill + "," + remainingAmount;

        // Save to file
        try (FileWriter writer = new FileWriter(PATIENT_FILE, true)) {  
            writer.write(patientData + "\n");
            System.out.println(" Patient added successfully!\n");
        } catch (IOException e) {
            System.out.println(" Error saving patient data: " + e.getMessage());
        }
    }

    // Method to Generate current date & time, split into Date and Time in 24-hour format
    private static String[] getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");  // 24-hour format without AM/PM
        
        String[] dateTime = new String[2];
        dateTime[0] = now.format(formatterDate);  // Date
        dateTime[1] = now.format(formatterTime);  // Time (24-hour format)
        
        return dateTime;
    }

    //Method to check whether the patient ID already exist 
    private static boolean isPatientIdTaken(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATIENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 10 && details[0].trim().equals(id)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println(" Error reading patient data: " + e.getMessage());
        }
        return false;
    }

    // Check if the room is available
    private static boolean isRoomAvailable(String roomNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ROOM_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 2 && details[0].trim().equals(roomNumber) && details[1].trim().equalsIgnoreCase("Available")) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading room data: " + e.getMessage());
        }
        return false;
    }

    //Upate room function 
    private static void updateRoomStatus(String roomNumber, String status) {
        File file = new File(ROOM_FILE);
        File tempFile = new File("temp_rooms.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             FileWriter writer = new FileWriter(tempFile)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 2 && details[0].trim().equals(roomNumber)) {
                    writer.write(roomNumber + "," + status + "," + details[2] + "," + details[3] + "\n");
                } else {
                    writer.write(line + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println(" Error updating room status: " + e.getMessage());
            return;
        }

        // Replace old file with updated file
        if (file.delete() && tempFile.renameTo(file)) {
            System.out.println(" Room status updated.");
        } else {
            System.out.println(" Error updating room file.");
        }
    }
}