import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

public class lifespan {

    private String CountryISO(String country) {
        return "KES";
    }

    private String TimeLeft(String diagnosticdate) {
        return "01012020";
    }

    // User class
    public abstract class User{
        static String uid = String.valueOf(new Random().nextInt(1000));
        static String registrationcomplete = "1";
        static String role = "patient";
        static String email="email", password="password", firstname="firstname", lastname="lastname", dob="dob", diagnosticdate="diagnosticdate", artdate="artdate", country="country", yltl="yltl";
    }

    public class Patient extends User {
        static String registrationcomplete = "0";
    }

    public class AdminUser extends User {
        static String role="admin";
    }
    
    public static void main(String [] args ) throws IOException, InterruptedException {
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
                    // Call Registration Complete method
                    lifespan.CompleteRegistration(scanner, menu_selection1, uidresult);
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
    public String LoginFlow(Scanner scanner) throws IOException, InterruptedException{        
        System.out.println(("-").repeat(100));
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        
        // Check if Password is in user file
        String result = CheckUserFile(email);        
        if (result.length()> 1) {
            System.out.println("Press Enter to input your password");
            String pwd = hashPassword(scanner.nextLine());
            
            // Check if Password is in user file
            String pwdresult = CheckUserFile(pwd);
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

    public void CompleteRegistration(Scanner scanner, String uid, String userObject) throws IOException, InterruptedException {
        System.out.println(("-").repeat(100));
        System.out.println("Complete your Registration");
        
        // create patient class instance
        Patient patient = new Patient();
        // Get values from existing data in users file
        String [] patientObj = userObject.split(" ");
        patient.uid = uid;
        patient.email = patientObj[1];
        patient.role = patientObj[3];
        patient.registrationcomplete = patientObj[4];

        // Check if user already completed registration
        if(patient.registrationcomplete.contains("1")){
            // Continue
            System.out.println("Registration Already Completed for User");
            LoginFlow(scanner);
        } else {
            // Return to Patient Page
            patient.registrationcomplete = "1";
            // Get values from user
            System.out.println("Please Enter your First Name: ");
            patient.firstname = scanner.nextLine();
            System.out.println("Please Enter your Last Name: ");
            patient.lastname = scanner.nextLine();
            System.out.println("Please Enter your Date of Birth in the format DDMMYYYY: ");
            patient.dob = scanner.nextLine();
            System.out.println("Have you tested positive for HIV? Y / N: ");
            String hivStatus = scanner.nextLine();
            switch(hivStatus.toUpperCase()){
                case "Y":
                    System.out.println("What date did you receive your results in the format DDMMYYYY: ");
                    patient.diagnosticdate = scanner.nextLine();
                    break;
                case "N":
                    break;
                default:
                    System.out.print("Invalid Input");
                    PatientPage(scanner, patient.email);
            }
            System.out.println("Are you on Anti Retroviral Treatment? Y / N: ");
            String arvstatus = scanner.nextLine();
            switch(arvstatus.toUpperCase()){
                case "Y":
                    System.out.println("What date did you start your treatment in the format DDMMYYYY: ");
                    patient.artdate = scanner.nextLine();
                    break;
                case "N":
                    break;
                default:
                    System.out.print("Invalid Input");
                    PatientPage(scanner, patient.email);
            }
            // Calculate time left to live
            patient.yltl = patient.diagnosticdate;
            System.out.println("What is your country of Residence: ");
            patient.country = scanner.nextLine();
            System.out.println("Press Enter to set a new password");
            patient.password = hashPassword(scanner.nextLine());
            
            //Replace this line in file
            ProcessBuilder pb1 = new ProcessBuilder().redirectErrorStream(true);        
            pb1.command("./update_user.sh", patient.uid, patient.email, patient.password, patient.role, patient.registrationcomplete, patient.firstname, patient.lastname, patient.dob, patient.diagnosticdate, 
                patient.artdate, patient.country, patient.yltl);   
            
            Process p1 = pb1.start();
            String procresult = new String(p1.getInputStream().readAllBytes());
            
            if(procresult.contains("0")){
                //success
                System.out.println("User Updated");
                PatientPage(scanner, patient.email);
            } else {                
                System.out.println("ERROR User Update Failed");
                // Call Admin profile page
                LandingPage(scanner);
            }
        }
    }

    public void ViewUsers(Scanner scanner) throws IOException{
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("./viewusers.sh");
        Process p = pb.start();
        String result = new String(p.getInputStream().readAllBytes());
        System.out.println(result);
        AdminPage(scanner);
    }

    // Hash password using OpenSSL
    static String hashPassword(String password) throws IOException, InterruptedException {
        //String salt = generateSalt();
        //Using static salt value
        String salt = "5414b2ad-1660-4065-8e41-d5b3d2781770";
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
    
}
