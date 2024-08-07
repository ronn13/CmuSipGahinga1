import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Lifespan {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome To LifeSpan.");
        LandingPage(scanner);
    }

    // Functionality to Check the User file for specific value        
    static String CheckUserFile(String uniqueid) throws IOException {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("./sample.sh", uniqueid);
        Process p = pb.start();
        String result = new String(p.getInputStream().readAllBytes());
        return result;
    }

    // Check if a user is Admin. Return 1 if Admin, 0 If otherwise
    static int CheckIfAdmin(String uniqueid) throws IOException {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("./role.sh", uniqueid);
        Process p = pb.start();
        String result = new String(p.getInputStream().readAllBytes());
        return result.length() > 1 ? 1 : 0;
    }

    // Landing Page
    static void LandingPage(Scanner scanner) throws IOException, InterruptedException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type 1 To Complete Your Registration. Type 2 To Login:");
        String menuSelection = br.readLine();

        switch (menuSelection) {
            case "1":
                System.out.println("Enter your User ID:");
                String userId = br.readLine();
                String uidResult = CheckUserFile(userId);
                if (uidResult.length() > 1) {
                    System.out.println("User ID Exists");
                } else {
                    System.out.println("User ID Does not exist. Please contact Admin or Try Again");
                }
                break;
            case "2":
                LoginFlow(scanner);
                break;
            default:
                System.out.println("Invalid Selection " + menuSelection);
        }
    }

    // Login Flow
    static void LoginFlow(Scanner scanner) throws IOException, InterruptedException {
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        String result = CheckUserFile(email);
        if (result.length() > 1) {
            Console console = System.console();
            char[] pwd = console.readPassword("Password: ");
            String pwdResult = CheckUserFile(hashPassword(String.valueOf(pwd)));
            if (pwdResult.length() > 1) {
                System.out.println("Welcome!");
                int role = CheckIfAdmin(email);
                if (role == 1) {
                    AdminPage(scanner);
                } else {
                    PatientPage(scanner);
                }
            } else {
                System.out.println("Invalid Password. Please check and Login Again");
                LandingPage(scanner);
            }
        } else {
            System.out.println("Invalid Email");
            LandingPage(scanner);
        }
    }

    static void PatientPage(Scanner scanner) throws IOException {
        System.out.println("Type 1 To View Your Profile. Type 2 To Update Your Profile:");
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                Patient patient = getPatientData();
                patient.viewProfile();
                break;
            case "2":
                System.out.println("Enter new Date of Birth (DD-MM-YYYY):");
                String dob = scanner.nextLine();
                System.out.println("Enter new Diagnosis Date (DD-MM-YYYY):");
                String diagDate = scanner.nextLine();
                System.out.println("On ART (true/false):");
                boolean onART = Boolean.parseBoolean(scanner.nextLine());
                System.out.println("Enter new ART Start Date (DD-MM-YYYY):");
                String artDate = scanner.nextLine();
                System.out.println("Enter new Country Code:");
                String country = scanner.nextLine();
                Patient currentPatient = getPatientData();
                currentPatient.updateProfile(dob, diagDate, onART, artDate, country);
                System.out.println("Profile Updated Successfully.");
                break;
            default:
                System.out.println("Invalid Selection.");
        }
    }

    static void AdminPage(Scanner scanner) throws IOException, InterruptedException {
        System.out.println("Type 1 To View a Profile. Type 2 To Update a Profile. Type 3 Export Data. Type 4 To Initiate a Registration:");
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                System.out.println("Profile view not yet designed");
                break;
            case "2":
                System.out.println("Update view not yet designed");
                break;
            case "3":
                exportUserData();
                break;
            case "4":
                InitRegistration(scanner);
                break;
            default:
                System.out.println("Invalid selection");
        }
    }

    // Initiate Registration
    static void InitRegistration(Scanner scanner) throws IOException, InterruptedException {
        System.out.println("Register User by entering the user's email: ");
        String email = scanner.nextLine();
        String result = CheckUserFile(email);
        if (result.length() > 1) {
            System.out.println("User Already exists");
            AdminPage(scanner);
        } else {
            String uid = String.valueOf(new Random().nextInt(1000));
            ProcessBuilder pb = new ProcessBuilder();
            pb.command("./add_user.sh", uid, email, hashPassword("default"), "patient", "0", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL");
            Process p = pb.start();
            String procResult = new String(p.getInputStream().readAllBytes());
            if (procResult.contains("0")) {
                System.out.println("New User created with uid " + uid);
                AdminPage(scanner);
            } else {
                System.out.println("ERROR User Creation Failed");
                AdminPage(scanner);
            }
        }
    }

    // Export user data for admin
    static void exportUserData() {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("bash", "-c", "./export_data.sh");
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Data Exported Successfully.");
            } else {
                System.out.println("Error in Data Export.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Hash password using OpenSSL
    static String hashPassword(String password) throws IOException, InterruptedException {
        String salt = generateSalt();
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("bash", "-c", String.format("echo -n \"%s\" | openssl passwd -6 -salt %s", password, salt));
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();
        process.waitFor();
        return line;
    }

    // Generate a random salt for hashing
    static String generateSalt() {
        Random rand = new Random();
        byte[] saltBytes = new byte[4];
        rand.nextBytes(saltBytes);
        StringBuilder salt = new StringBuilder();
        for (byte b : saltBytes) {
            salt.append(String.format("%02x", b));
        }
        return salt.toString();
    }

    // Mock method to fetch patient data, implement actual method to fetch data
    static Patient getPatientData() {
        // Implementation to fetch and return patient object
        return new Patient(); // placeholder
    }
}
