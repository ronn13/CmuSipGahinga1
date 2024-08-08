import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class LifespanTest {

    // Method to calculate remaining lifespan using session variables
    public int calculateLifespan(String email, String password) throws IOException, InterruptedException {
        // Retrieve the user data from the file using the email and password
        String userData = getUserData(email, password);
        if (userData == null || userData.isEmpty()) {
            System.out.println("User not found or invalid credentials.");
            return -1;
        }

        // Parse the user data
        String[] userFields = userData.split("\\s+");
        String dob = userFields[7];
        String diagnosticDate = userFields[8];
        String artDate = userFields[9];
        int averageLifespan = Integer.parseInt(userFields[10]);

        // Calculate the age
        int birthYear = Integer.parseInt(dob.substring(4, 8));
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int age = currentYear - birthYear;

        // Determine diagnosis and ART start year
        int diagnosisYear = Integer.parseInt(diagnosticDate.substring(4, 8));
        int artStartYear = artDate.equals("NULL") ? 0 : Integer.parseInt(artDate.substring(4, 8));

        // Calculate remaining lifespan
        return calculateLifespan(age, diagnosisYear, artStartYear, averageLifespan);
    }

    // Method to extract user data using bash commands
    private String getUserData(String email, String password) throws IOException, InterruptedException {
        // Hash the input password to compare with the stored hash
        ProcessBuilder hashPb = new ProcessBuilder("bash", "-c", String.format("echo -n '%s' | openssl passwd -6", password));
        Process hashProcess = hashPb.start();
        BufferedReader hashReader = new BufferedReader(new InputStreamReader(hashProcess.getInputStream()));
        String hashedPassword = hashReader.readLine();
        hashProcess.waitFor();

        // Retrieve user data using the hashed password
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", String.format("grep -w '%s' user-store.txt | grep -w '%s'", email, hashedPassword));
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String userData = reader.readLine();
        process.waitFor();
        return userData;
    }

    // Original calculateLifespan method (unchanged)
    public int calculateLifespan(int age, int diagnosisYear, int artStartYear, int averageLifespan) {
        if (artStartYear == 0) { // Not on ART drugs
            return Math.max(5, 0);
        }
        int remainingYears = averageLifespan - age;
        double survivalRate = 0.9;
        int yearsOnART = artStartYear - diagnosisYear;
        for (int i = 0; i < yearsOnART; i++) {
            remainingYears *= survivalRate;
        }
        return Math.max((int) Math.ceil(remainingYears), 0);
    }

    // Main method for testing
    public static void main(String[] args) {
        LifespanTest lifespanCalculator = new LifespanTest();
        String testEmail = "janedoe@gmail.com";
        String testPassword = "pa55word";

        try {
            // Calculate lifespan using session variables
            int remainingLifespan = lifespanCalculator.calculateLifespan(testEmail, testPassword);
            System.out.println("Remaining Lifespan: " + remainingLifespan + " years.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
