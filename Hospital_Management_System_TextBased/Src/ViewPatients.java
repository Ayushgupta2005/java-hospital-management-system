import java.io.*;

public class ViewPatients implements Displayable {
    private static final String FILE_PATH = "/Users/ayushgupta/Desktop/Hospital Management System_Text based /data/patients.txt";

    public void displayData() {
        File file = new File(FILE_PATH);

        // Check if the file exists or is empty
        if (!file.exists()) {
            System.out.println(" Error: File does not exist at " + FILE_PATH);
            return;
        }
        if (file.length() == 0) {
            System.out.println(" No patient records found (File is empty).");
            return;
        }

        System.out.println("\n===== Patient Records =====");
        System.out.printf("%-10s %-20s %-5s %-10s %-20s %-10s %-10s %-15s %-12s %-12s %-12s%n", 
                "ID", "Name", "Age", "Gender", "Disease", "Deposit", "Room No", "Date", "Time", "Total Bill", "Remaining Amount");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");

        // Read and display the file contents
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean hasRecords = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                // Split into 11 parts 
                String[] details = line.split(",", 11);

                // Ensure the record has exactly 11 fields
                if (details.length == 11) {  
                    hasRecords = true;
                    System.out.printf("%-10s %-20s %-5s %-10s %-20s %-10s %-10s %-15s %-12s %-12s %-12s%n",
                            details[0].trim(), details[1].trim(), details[2].trim(), 
                            details[3].trim(), details[4].trim(), details[5].trim(), details[6].trim(),
                            details[7].trim(), details[8].trim(), details[9].trim(), details[10].trim());
                } else {
                    System.out.println(" Skipping invalid record: " + line);
                }
            }

            if (!hasRecords) {
                System.out.println("\n No valid patient records found.");
            }

        } catch (IOException e) {
            System.out.println(" Error reading patient data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ViewPatients viewPatients = new ViewPatients();
        viewPatients.displayData();
    }
}