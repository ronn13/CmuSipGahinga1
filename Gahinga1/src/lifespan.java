import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

public class lifespan {

    // User class
    public abstract class User{
        static String uid = String.valueOf(new Random().nextInt(1000));
        static String registrationcomplete = "1";
        static String role = "patient";
        static String email="NULL", password="NULL", firstname="NULL", lastname="NULL", dob="NULL", diagnosticdate="NULL", artdate="NULL", country="NULL", yltl="NULL";
    }

    public class Patient extends User {
        static String registrationcomplete = "0";
    }

    public class AdminUser extends User {
        static String role="admin";
    }
    
    public static void main(String [] args ) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome To LifeSpan.");
        
        // Load Landing Page
        lifespan lifespan = new lifespan();
        String menu_selection = lifespan.LandingPage(scanner);
        switch (menu_selection) {
            case "1": 
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));    
                System.out.println("Enter your User ID:");
                String menu_selection1 = br1.readLine();
                //search for uid                    
                String uidresult = lifespan.CheckUserFile(menu_selection1);
                if(uidresult.length()>1){
                    // Call User Profile Update Method
                    System.out.println("User ID Exists");
                } else {
                    System.out.println("User ID Does not exist. Please contact Admin or Try Again");
                }
                break;                                   
            case "2": 
                //Call Login Method
                String login_result = lifespan.LoginFlow(scanner);
                if(login_result.contains("ERROR")){
                    System.out.println(login_result);
                } else {
                    //Check role
                    int role = lifespan.CheckIfAdmin(login_result);
                    if(role==1){
                        // Load Admin Home page
                        lifespan.AdminPage(scanner);
                    } else {
                        // Load Patient Home Page
                        lifespan.PatientPage(scanner, login_result);
                    }
                }
                
                break;
            default:
                System.out.println("Invalid Selection " + menu_selection);
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

    // Functionality to Check the User file for specific value        
    public String CheckUserFile(String uniqueid) throws IOException{
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("./sample.sh", uniqueid);
        Process p = pb.start();
        String result = new String(p.getInputStream().readAllBytes());
        return(result);
    }

    // Check if a user is Admin. Return 1 if Admin, 0 If otherwise
    public int CheckIfAdmin(String uniqueid) throws IOException{
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("./role.sh", uniqueid);
        Process p = pb.start();
        String result = new String(p.getInputStream().readAllBytes());
        //check exit type of processbuilder rather than check count of result
        if(result.length()>1){
            return 1;
        } else {
            return 0;
        } 
    }

    // Landing Page
    public String LandingPage(Scanner scanner) throws IOException{
        // Landing Page
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type 1 To Complete Your Registration. Type 2 To Login:");
        String menu_selection = br.readLine();
        return menu_selection;        
    }
    
    // Login Flow
    public String LoginFlow(Scanner scanner) throws IOException{        
        System.out.println(("-").repeat(100));
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        
        // Check if Password is in user file
        String result = CheckUserFile(email);        
        if (result.length()> 1) {
            Console cnsl = System.console();
            char[] pwd = cnsl.readPassword("Password: ");
            
            // Check if Password is in user file
            String pwdresult = CheckUserFile(String.valueOf(pwd));
            if (pwdresult.length()> 1) {
                System.out.println("Welcome!");
                return email;
            } else {
                return "ERROR:Invalid Password. Please check and Login Again";
            }
        } else {
            return "ERROR:Invalid Email. Please check and Login Again";
        }
    }

    // Patient Lading Page
    void PatientPage(Scanner scanner, String email) throws IOException{
        System.out.println(("_").repeat(100));
        System.out.println("Type 1 To View Your Profile. Type 2 To Update Your Profile. Type 3 To View Your Life Expectancy. Type 4 to exit:");
        String input = scanner.nextLine();
        switch(input){
            case "1":
                ViewProfile(scanner, email);
            case "2":
                System.out.println("Update view not yet designed");
            case "3":
                System.out.println("Life expectancy not yet designed");
            case "4":
                System.exit(0);
            default:
                System.out.println("User Already exists");

        }
    }

    // Admin Landing Page
    public void AdminPage(Scanner scanner) throws IOException{
        System.out.println(("-").repeat(100));
        System.out.println("Type 1 To View Users. Type 2 To Update a Profile. Type 3 Export Data. Type 4 To Initiate a Registration. Type 5 to exit:");
        String input = scanner.nextLine();
        switch(input){
            case "1":
                ViewUsers(scanner);
            case "2":
                System.out.println("Update view not yet designed");
            case "3":
                System.out.println("Export data not yet designed");
            case "4":
                InitRegistration(scanner);
            case "5":
                System.exit(0);
            default:
                System.out.println("Invalid Input");

        } 
    }

    //Initiate Registration
    public void InitRegistration(Scanner scanner) throws IOException{
        System.out.println(("-").repeat(100));
        System.out.println("Register User by entering the user's email: ");
        String email = scanner.nextLine();
        //If email does not already exist then create user
        String result = CheckUserFile(email);        
        if (result.length()> 1) {
            //Email exists
            System.out.println("User Already exists");
            // Call Admin profile page
            AdminPage(scanner);
        }
        else {
            Patient patient = new Patient();
            patient.email = email;
            ProcessBuilder pb = new ProcessBuilder();
            pb.command("./add_user.sh", patient.uid, patient.email, patient.password, patient.role, patient.registrationcomplete, patient.firstname, patient.lastname, patient.dob, patient.diagnosticdate, 
                patient.artdate, patient.country, patient.yltl);            
            Process p = pb.start();

            String procresult = new String(p.getInputStream().readAllBytes());
            //check exit type of processbuilder rather than check count of result
            if(procresult.contains("0")){
                //success
                System.out.println("New User created with uid " + patient.uid);
                // Call Admin profile page
                AdminPage(scanner);
            } else {                
                System.out.println("ERROR User Creation Failed");
                // Call Admin profile page
                AdminPage(scanner);
            }
        }
        
    }

    public void ViewProfile(Scanner scanner, String email) throws IOException {
        String result = CheckUserFile(email);
        System.out.println(result);
        PatientPage(scanner, email);
        
    }

    public void ViewUsers(Scanner scanner) throws IOException{
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("./viewusers.sh");
        Process p = pb.start();
        String result = new String(p.getInputStream().readAllBytes());
        System.out.println(result);
        AdminPage(scanner);
    }
    
}
