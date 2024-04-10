//package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PatientVisitsManager {


    public static List<String> extractDates(String filePath) throws IOException {
        List<String> dates = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isEmpty = true;
            while ((line = reader.readLine()) != null) {
                isEmpty = false;
                if (line.startsWith("Date:")) {
                    String date = line.substring("Date: ".length()).trim();
                    dates.add(date);
                }
            }
            // If the file is empty, return an empty list
            if (isEmpty) {
                return dates;
            }
        }

        return dates;
    }
}