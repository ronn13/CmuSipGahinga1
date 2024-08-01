import java.io.Console;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

public class lifespan {
    public static void main(String [] args ) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Generate a unique identifier (UUID)
        String uuid = UUID.randomUUID().toString();

        System.out.println("Enter email: ");
        String email = scanner.nextLine();

        ProcessBuilder pb = new ProcessBuilder();
        pb.command("./sample.sh", email);
        Process p = pb.start();
        String result = new String(p.getInputStream().readAllBytes());
        if (result.length()> 1) {
            // Check Password
            Console cnsl = System.console();
            char[] pwd = cnsl.readPassword("Password: ");
            ProcessBuilder pbpwd = new ProcessBuilder();
            pbpwd.command("./sample.sh", String.valueOf(pwd));
            Process ppwd = pbpwd.start();
            String pwdresult = new String(ppwd.getInputStream().readAllBytes());
            if (pwdresult.length()> 1) {
                System.out.println("Welcome!");
            } else {
                System.out.println("Invalid Password. Please check and Login Again");
            }
        } else {
            System.out.println("Invalid Email");
            // Call landing page
        }

//        String password = "yourPasswordHere";
//        String salt = "randomSalt"; // It's a good practice to generate a random salt
//
//        try {
//            // Construct the command to execute OpenSSL
//            String command = String.format("echo -n \"%s\" | openssl passwd -6 -salt %s", password, salt);
//
//            // Execute the command
//            Process process = Runtime.getRuntime().exec(new String[] { "bash", "-c", command });
//
//            // Read the output
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println("Hashed Password: " + line);
//            }
//
//            // Wait for the process to complete
//            int exitCode = process.waitFor();
//            if (exitCode != 0) {
//                System.err.println("Error: OpenSSL command failed with exit code " + exitCode);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



        /*
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        System.out.println("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.println("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.println("Enter date of birth (DD-MM-YYYY): ");
        String dateOfBirth = scanner.nextLine();

        System.out.println("Enter diagnostic date (YYYY-MM-DD): ");
        String diagnosticDate = scanner.nextLine();

        System.out.println("Enter ART date (YYYY-MM-DD): ");
        String artDate = scanner.nextLine();

        System.out.println("Enter country: ");
        String country = scanner.nextLine();

        System.out.println("Enter years left to live: ");
        int yearsLeftToLive = scanner.nextInt();

        // Display the collected information
        System.out.println("\nRegistration Information:");
        System.out.println("UUID: " + uuid);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("Diagnostic Date: " + diagnosticDate);
        System.out.println("ART Date: " + artDate);
        System.out.println("Country: " + country);
        System.out.println("Years Left to Live: " + yearsLeftToLive); */

        scanner.close();
    }
}
