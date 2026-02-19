

import java.util.Scanner;

public class Login {
    private static final String CORRECT_USERNAME = "HospitalManagement";
    private static final String CORRECT_PASSWORD = "12345";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isAuthenticated = false;

        System.out.println("===== Hospital Management System Login =====");

        while (!isAuthenticated) {
            System.out.print("Enter Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine().trim();

            if (username.equals(CORRECT_USERNAME) && password.equals(CORRECT_PASSWORD)) {
                System.out.println("\n Login Successful! Redirecting to Main Menu...\n");
                isAuthenticated = true;  
                MainMenu.display();  
            } else {
                System.out.println("\n Incorrect Username or Password. Try again.\n");
            }
        }

        scanner.close();
    }
}