import java.io.*;

public class Department implements Displayable {
    private static final String FILE_PATH = "/Users/ayushgupta/Desktop/Hospital Management System_Text based /data/departments.txt"; // ✅ Same directory

    public void displayData() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.out.println(" Error: Departments file does not exist at " + FILE_PATH);
            return;
        }

        if (file.length() == 0) {
            System.out.println(" No department records found (File is empty).");
            return;
        }

        System.out.println("\n===== Hospital Departments =====");
        System.out.printf("%-15s %-25s %-20s%n", "Dept ID", "Department Name", "Head of Department");
        System.out.println("------------------------------------------------------------------");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean hasRecords = false;

            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 3) {
                    hasRecords = true;
                    System.out.printf("%-15s %-25s %-20s%n", details[0], details[1], details[2]);
                } else {
                    System.out.println(" Skipping invalid record: " + line);
                }
            }

            if (!hasRecords) {
                System.out.println("\n No valid department records found.");
            }

        } catch (IOException e) {
            System.out.println(" Error reading department data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Department department = new Department();
        department.displayData();
    }
}