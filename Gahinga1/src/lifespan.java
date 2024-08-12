import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;
//import java.util.stream.Gatherer;

public class lifespan {

    public int TimeLeft(String country, String dob, String artdate, String diagdate) throws IOException, InterruptedException {
        
        //Get Avg Lifespan from country list
        String CtryObj = CheckCountry(country);
        String [] CtryObjArray = CtryObj.split(",");
        float averageLifespan = Float.parseFloat(CtryObjArray[6]);

        // Calculate the age
        int birthYear = Integer.parseInt(dob.substring(4, 8));
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int age = currentYear - birthYear;

        // Determine diagnosis and ART start year
        int diagnosisYear = Integer.parseInt(diagdate.substring(4, 8));
        int artStartYear = artdate.equals("NULL") ? 0 : Integer.parseInt(artdate.substring(4, 8));

        // Calculate remaining lifespan
        if (artStartYear == 0) { // Not on ART drugs
            return Math.max(5, 0);
        }
        int remainingYears = Math.round(averageLifespan) - age;
        double survivalRate = 0.9;
        int yearsOnART = artStartYear - diagnosisYear;
        for (int i = 0; i < yearsOnART; i++) {
            remainingYears *= survivalRate;
        }
        return Math.max((int) Math.ceil(remainingYears), 0);
    }
    
    // User class
    public abstract class User{
        static String uid = String.valueOf(new Random().nextInt(1000));
        static String registrationcomplete = "1";
        static String role = "patient";
        static String email="email", password="password", firstname="firstname", lastname="lastname", dob="NULL", diagnosticdate="NULL", artdate="NULL", country="country", yltl="yltl";
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
                if(uidresult.length()>10){
                    // Call Registration Complete method
                    lifespan.CompleteRegistration(scanner, menu_selection1, uidresult);
                } else {
                    System.out.println("User ID Does not exist. Please contact Admin or Try Again");
                }
                break;                                   
            case "2": 
                //Call Login Method
                String userObj = lifespan.LoginFlow(scanner);
                if(userObj.contains("ERROR")){
                    System.out.println(userObj);
                } else {
                    //Check role
                    if(userObj.split(" ")[3].contains("admin")){
                        // Load Admin Home page
                        lifespan.AdminPage(scanner, userObj);
                    } else {
                        // Load Patient Home Page
                        lifespan.PatientPage(scanner, userObj);
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

    // Functionality to Check the User file for specific value        
    public String CheckCountry(String country) throws IOException{
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("./country_search.sh", country);
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
        System.out.println("Enter your email and password to log into the LifeSpan system");
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        
        // Check if Password is in user file
        String userObj = CheckUserFile(email);        
        if (userObj.length()> 10) {
            System.out.println("Press Enter to input your password");
            String pwd = hashPassword(scanner.nextLine());
            
            // Check if password from file matches hashed user input is in user file
            String [] patientObj = userObj.split(" ");
            String dbpwd = patientObj[2];
            if (pwd.equals(dbpwd)) {
                System.out.println("Welcome!");
                return userObj;
            } else {
                return "ERROR:Invalid Password. Please check and Login Again";
            }
        } else {
            return "ERROR:Invalid Email. Please check and Login Again";
        }
    }

    // Patient Lading Page
    void PatientPage(Scanner scanner, String obj) throws IOException, InterruptedException{
        System.out.println(("_").repeat(100));
        System.out.println("Type 1 To View Your Profile. Type 2 To Update Your Profile. Type 3 To View Your Life Expectancy. Type 4 to exit:");
        String input = scanner.nextLine();
        switch(input){
            case "1":
                ViewProfile(scanner, obj);
            case "2":
                UpdateProfile(scanner, obj);
            case "3":
                System.out.println("Life expectancy not yet designed");
            case "4":
                System.exit(0);
            default:
                System.out.println("User Already exists");

        }
    }

    // Admin Landing Page
    public void AdminPage(Scanner scanner, String userObj) throws IOException{
        System.out.println(("-").repeat(100));
        System.out.println("Type 1 To View Users. Type 2 Export Data. Type 3 To Initiate a Registration. Type 4 to exit:");
        String input = scanner.nextLine();
        switch(input){
            case "1":
                ViewUsers(scanner, userObj);
                break;
            case "2":
                exportUserData(scanner, userObj);                
                break;
            case "3":
                InitRegistration(scanner);
                break;
            case "4":
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
        if (result.length()> 10) {
            //Email exists
            System.out.println("User Already exists");
            // Call Admin profile page
            AdminPage(scanner, result);
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
                AdminPage(scanner, result);
            } else {                
                System.out.println("ERROR User Creation Failed");
                // Call Admin profile page
                AdminPage(scanner, result);
            }
        }
        
    }

    public void ViewProfile(Scanner scanner, String obj) throws IOException, InterruptedException {
        System.out.println(obj);
        PatientPage(scanner, obj);
        
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
                    System.exit(0);
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
                    System.exit(0);
            }
            
            System.out.println(String.format("Update your country of Residence %s: ", patientObj[10]));
            patient.country = scanner.nextLine();

            // Validate country
            while(CheckCountry(patient.country).length() < 5){
                System.out.println("Invalid Country Value " + patient.country);
                System.out.println("Update your country of Residence: ");
                patient.country = scanner.nextLine();
            }

            // Calculate time left to live
            patient.yltl = String.valueOf(TimeLeft(patient.country, patient.dob, patient.artdate, patient.diagnosticdate));

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
                System.exit(0);
            } else {                
                System.out.println("ERROR User Update Failed");
                // Call Admin profile page
                System.exit(0);
            }
        }
    }

    public void UpdateProfile(Scanner scanner, String obj) throws IOException, InterruptedException{
        System.out.println(("-").repeat(100));
        
        // create patient class instance
        Patient patient = new Patient();
        // Get values from existing data in users file
        String [] patientObj = obj.split(" ");
        patient.uid = patientObj[0];
        patient.email = patientObj[1];
        patient.role = patientObj[3];
        patient.registrationcomplete = patientObj[4];
        patient.password = patientObj[2];

        // Get values from user
        System.out.println(String.format("Please Update your First Name %s: ", patientObj[5]));
        patient.firstname = scanner.nextLine();
        System.out.println(String.format("Please Update your Last Name %s: ", patientObj[6]));
        patient.lastname = scanner.nextLine();
        System.out.println(String.format("Please Update your Date of Birth %s in the format DDMMYYYY: ", patientObj[7]));
        patient.dob = scanner.nextLine();
        System.out.println(String.format("Update your diagnosis Date %s in the format DDMMYYYY: ", patientObj[8]));
        patient.diagnosticdate = scanner.nextLine();
        System.out.println(String.format("Update the date you begain ART Treatment %s in the format DDMMYYYY: ", patientObj[9]));
        patient.artdate = scanner.nextLine();
        System.out.println(String.format("Update your country of Residence %s: ", patientObj[10]));
        patient.country = scanner.nextLine();

        // Validate country
        while(CheckCountry(patient.country).length() < 5){
            System.out.println("Invalid Country Value " + patient.country);
            System.out.println("Update your country of Residence: ");
            patient.country = scanner.nextLine();
        }

        // Calculate time left to live
        patient.yltl = String.valueOf(TimeLeft(patient.country, patient.dob, patient.artdate, patient.diagnosticdate));
        
        //Replace this line in file
        ProcessBuilder pb1 = new ProcessBuilder().redirectErrorStream(true);        
        pb1.command("./update_user.sh", patient.uid, patient.email, patient.password, patient.role, patient.registrationcomplete, patient.firstname, patient.lastname, patient.dob, patient.diagnosticdate, 
            patient.artdate, patient.country, patient.yltl);   
        
        Process p1 = pb1.start();
        String procresult = new String(p1.getInputStream().readAllBytes());
        
        if(procresult.contains("0")){
            //success
            System.out.println("User Updated");
            PatientPage(scanner, obj);
        } else {                
            System.out.println("ERROR User Update Failed");
            // Call Admin profile page
            PatientPage(scanner, obj);
        }
    }

    public void ViewUsers(Scanner scanner, String userObj) throws IOException{
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("./viewusers.sh");
        Process p = pb.start();
        String result = new String(p.getInputStream().readAllBytes());
        System.out.println(result);
        AdminPage(scanner, userObj);
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

    // Export user data for admin
    public void exportUserData(Scanner scanner, String userObj) {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("bash", "-c", String.format("./export_data.sh"));
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Data Exported Successfully to CSV.");
                AdminPage(scanner, userObj);
            } else {
                System.out.println("Error in Data Export.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
