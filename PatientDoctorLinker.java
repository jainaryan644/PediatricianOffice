//package application;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PatientDoctorLinker {

    static class Doctor {
        String username;
        String name;
        String email;

        Doctor(String username, String name, String email) {
            this.username = username;
            this.name = name;
            this.email = email;
        }

        @Override
        public String toString() {
            return "Doctor Name: " + this.name + ", Email: " + this.email;
        }
    }
    
    public static String getRoleName(String role) {
        String filePath;
        
        if(role.equalsIgnoreCase("Doctor")) {
            filePath = "doctors.txt"; 
        } else if(role.equalsIgnoreCase("Nurse")) {
            filePath = "nurses.txt";
        } else {
            return "Invalid role specified.";
        }
        
        List<String> names = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                if (line.startsWith("Name:")) {
                    // Directly add the name to the list after extracting it
                    names.add(line.substring("Name:".length()).trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading file information.";
        }

        if (!names.isEmpty()) {
            // Randomly select a name for the role
            Random random = new Random();
            return names.get(random.nextInt(names.size()));
        } else {
            return "No names found for the specified role.";
        }
    }

    public static String linkDoctorAndPatient(String role) {
    	String filePath;
    	
    	if(role.equals("Doctor"))
    	{
    		filePath = "doctors.txt"; 
    	}
    	
    	else
    	{
    		filePath = "nurses.txt";
    	}
    	
        // Ensure this path is correct.
        List<Doctor> doctors = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            // Assuming every doctor's info is separated by an empty line or at the end of the file.
            String username = "", name = "", email = "";
            for (String line : lines) {
                if (line.contains("%")) {
                    username = line.split("%")[1]; // Extracting username assuming it's after '%'.
                } else if (line.startsWith("Name:")) {
                    name = line.substring("Name:".length()).trim();
                } else if (line.startsWith("Email:")) {
                    email = line.substring("Email:".length()).trim();
                }
                
                // When we have collected all three pieces of information, add the doctor and reset for the next one.
                if (!username.isEmpty() && !name.isEmpty() && !email.isEmpty()) {
                    doctors.add(new Doctor(username, name, email));
                    // Reset for the next doctor
                    username = "";
                    name = "";
                    email = "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading doctor information.";
        }

        if (!doctors.isEmpty()) {
            // Randomly select a doctor for the patient
            Random random = new Random();
            Doctor selectedDoctor = doctors.get(random.nextInt(doctors.size()));
            return selectedDoctor.toString();
        } else {
            return "No doctor found.";
        }
    }
    
 
}
