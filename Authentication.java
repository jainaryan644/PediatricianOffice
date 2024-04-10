//package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Authentication {

    public static boolean authenticate(String role, String username, String password) {
        String filePath;
        switch (role) {
            case "doctor":
                filePath = "doctors.txt";
                break;
            case "nurse":
                filePath = "nurses.txt";
                break;
            case "patient":
                filePath = "patients.txt";
                break;
            default:
                return false; // Invalid role
        }

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String[] credentials = scanner.nextLine().split("%");
                if (credentials[0].trim().equals(username) && credentials[1].trim().equals(password)) {
                    return true; // Credentials match
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false; // Credentials do not match or file not found
    }
    
}
