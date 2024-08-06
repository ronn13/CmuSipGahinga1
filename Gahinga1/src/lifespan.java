import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

public class lifespan {
    public static void main(String [] args ) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome To LifeSpan.");
        
        // Load Landing Page
        LandingPage(scanner);

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
    static String CheckUserFile(String uniqueid) throws IOException{
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("./sample.sh", uniqueid);
        Process p = pb.start();
        String result = new String(p.getInputStream().readAllBytes());
        return(result);
    }

    // Check if a user is Admin. Return 1 if Admin, 0 If otherwise
    static int CheckIfAdmin(String uniqueid) throws IOException{
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
    static  void LandingPage(Scanner scanner) throws IOException{
        // Landing Page
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type 1 To Complete Your Registration. Type 2 To Login:");
        String menu_selection = br.readLine();

        switch (menu_selection) {
            case "1": 
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));    
                System.out.println("Enter your User ID:");
                String menu_selection1 = br1.readLine();
                //search for uid                    
                String uidresult = CheckUserFile(menu_selection1);
                if(uidresult.length()>1){
                    // Call User Profile Update Method
                    System.out.println("User ID Exists");
                } else {
                    System.out.println("User ID Does not exist. Please contact Admin or Try Again");
                }
                break;                                   
            case "2": 
                //Call Login Method
                LoginFlow(scanner);
                break;
            default:
                System.out.println("Invalid Selection " + menu_selection);
        }
    }
    
    // Login Flow
    static void LoginFlow(Scanner scanner) throws IOException{        
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
                //Check role
                int role = CheckIfAdmin(email);
                System.out.println(role);
                if(role==1){
                    // Load Admin Home page
                    AdminPage(scanner);
                } else {
                    // Load Patient Home Page
                    PatientPage(scanner);
                }
            } else {
                System.out.println("Invalid Password. Please check and Login Again");
                // Call landing page
                LandingPage(scanner);
            }
        } else {
            System.out.println("Invalid Email");
            // Call landing page
            LandingPage(scanner);
        }
    }

    // Patient Options Page
    static void PatientPage(Scanner scanner) throws IOException{
        System.out.println("Type 1 To View Your Profile. Type 2 To Update Your Profile. Type 3 To View Your Life Expectancy:");
    }

    // Admin Options Page
    static void AdminPage(Scanner scanner) throws IOException{
        System.out.println("Type 1 To View a Profile. Type 2 To Update a Profile. Type 3 Export Data. Type 4 To Initiate a Registration:");
        String input = scanner.nextLine();
        switch(input){
            case "1":
                System.out.println("Profile view not yet designed");
            case "2":
                System.out.println("Update view not yet designed");
            case "3":
                System.out.println("Export data not yet designed");
            case "4":
                InitRegistration(scanner);
            default:
                System.out.println("User Already exists");

        } 
    }

    //Initiate Registration
    static void InitRegistration(Scanner scanner) throws IOException{
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
            //Is this user an admin
            // System.out.println("Is this user an Admin? Y or N: ");
            // String IsAdmin = scanner.nextLine();
            // if (IsAdmin=="Y") {
            //     String Role = "admin";
            // } else {
            //     String Role = "patient";
            // }

            //Email does not exist so create recrod
            String uid = String.valueOf(new Random().nextInt(1000));

            ProcessBuilder pb = new ProcessBuilder();
            pb.command("./add_user.sh", uid, email, "NULL", "patient", "0", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL");            
            Process p = pb.start();

            String procresult = new String(p.getInputStream().readAllBytes());
            //check exit type of processbuilder rather than check count of result
            if(procresult.contains("0")){
                //success
                System.out.println("New User created with uid " + uid);
                // Call Admin profile page
                AdminPage(scanner);
            } else {                
                System.out.println("ERROR User Creation Failed");
                // Call Admin profile page
                AdminPage(scanner);
            }
        }
        
    }
}
