import java.io.*;

public class Room implements Displayable {
    private static final String ROOM_FILE = "/Users/ayushgupta/Desktop/Hospital Management System_Text based /data/rooms.txt";

    public void displayData() {
        File file = new File(ROOM_FILE);

        // Checking if file exists
        if (!file.exists() || file.length() == 0) {
            System.out.println(" No room records found.");
            return;
        }

        System.out.println("\n===== Room Information =====");
        System.out.printf("%-10s %-15s %-10s %-20s%n", "Room No", "Availability", "Price", "Room Type");
        System.out.println("--------------------------------------------------------");

        // Read and display room data
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 4) {
                    String roomNo = details[0].trim();
                    String availability = details[1].trim();
                    String price = details[2].trim();
                    String roomType = details[3].trim();

                    // If the room is occupied, display a message
                    if (availability.equalsIgnoreCase("Occupied")) {
                        System.out.printf("%-10s %-15s %-10s %-20s  Room is Occupied!%n", roomNo, availability, price, roomType);
                    } else {
                        System.out.printf("%-10s %-15s %-10s %-20s%n", roomNo, availability, price, roomType);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(" Error reading room data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Room room = new Room();
        room.displayData();
    }
}