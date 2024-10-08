import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

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

        // Clear screen 
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        System.out.println(("-").repeat(100));
        System.out.println((" ").repeat(40) + "Welcome To LifeSpan.");
        System.out.println(("-").repeat(100));
        
        // Load Landing Page
        lifespan lifespan = new lifespan();
        String menu_selection = lifespan.LandingPage(scanner);
        switch (menu_selection) {
            case "1": 
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));    
                System.out.print("Enter your User ID:");
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
            case "3":
                System.exit(0);
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
        System.out.println(("-").repeat(100));
        System.out.println((" ").repeat(40) + "Menu Options");
        System.out.println(("-").repeat(100));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("1. To Complete Your Registration");
        System.out.println("2. To Login:");
        System.out.println("3. To exit:");
        String menu_selection = br.readLine();
        return menu_selection;        
    }
    
    // Login Flow
    public String LoginFlow(Scanner scanner) throws IOException, InterruptedException{        
        System.out.println(("-").repeat(100));
        System.out.println("Enter your email and password to log into the LifeSpan system");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        // Check if Password is in user file
        String userObj = CheckUserFile(email);        
        if (userObj.length()> 10) {
            System.out.print("Press Enter to input your password");
            String pwd = hashPassword(scanner.nextLine());
            
            // Check if password from file matches hashed user input is in user file
            String [] patientObj = userObj.split(" ");
            String dbpwd = patientObj[2];
            if (pwd.equals(dbpwd)) {
                System.out.println(("_").repeat(100));
                System.out.println((" ").repeat(40) + "Welcome!");
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
        // Clear screen 
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println(("_").repeat(100));
        System.out.println((" ").repeat(40) + "Patient Menu Options");
        System.out.println(("_").repeat(100));
        
        System.out.println("1. To View Your Profile");
        System.out.println("2. To Update Your Profile");
        System.out.println("3. To View Your Life Expectancy");
        System.out.println("4. To Quit:");
        String input = scanner.nextLine();
        switch(input){
            case "1":
                ViewProfile(scanner, obj);
                break;
            case "2":
                UpdateProfile(scanner, obj);
                break;
            case "3":
                String [] patientObj = obj.split(" ");
                System.out.println(String.format("Death expected in (years) %s ", patientObj[patientObj.length-1]));
                break;
            case "4":
                // System.out.println("Returning to Landing Page...");
                // TimeUnit.SECONDS.sleep(2);
                // LandingPage(scanner);
                // break;
                System.exit(0);
            default:
                System.out.println("User Already exists");
                break;

        }
    }

    // Admin Landing Page
    public void AdminPage(Scanner scanner, String userObj) throws IOException, InterruptedException{
        // Clear screen 
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println(("-").repeat(100));
        System.out.println((" ").repeat(40) + "Admin Menu Options");
        System.out.println(("-").repeat(100));
        
        //System.out.println("1. To View Users");
        System.out.println("1. To Export User Data");
        System.out.println("2. To Initiate a Registration");
        System.out.println("3. To Quit:");
        String input = scanner.nextLine();
        switch(input){
            case "1":
                exportUserData(scanner, userObj);                
                break;
            case "2":
                InitRegistration(scanner);
                break;
            case "3":
                // System.out.println("...Logging Out.....");
                // TimeUnit.SECONDS.sleep(1);
                // LandingPage(scanner);
                // break;
                System.exit(0);
            default:
                System.out.println("Invalid Input");
                break;

        } 
    }

    //Initiate Registration
    public void InitRegistration(Scanner scanner) throws IOException, InterruptedException{
        // Clear screen 
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println(("-").repeat(100));
        System.out.println((" ").repeat(40) + "Register New User");
        System.out.println(("-").repeat(100));

        System.out.print("Register User by entering the user's email: ");
        String email = scanner.nextLine();
        //If email does not already exist then create user
        String result = CheckUserFile(email);        
        if (result.length()> 10) {
            //Email exists
            System.out.println("User Already exists");
            System.out.println("Press Enter to continue...");
            Scanner scanr = new Scanner(System.in);
            scanr.nextLine(); // Waits for the user to press Enter
            System.out.println("Continuing...");
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
                System.out.println("Press Enter to continue...");
                Scanner scanr = new Scanner(System.in);
                scanr.nextLine(); // Waits for the user to press Enter
                System.out.println("Continuing...");
                // Call Admin profile page
                AdminPage(scanner, result);
            } else {                
                System.out.println("ERROR User Creation Failed");
                System.out.println("Press Enter to continue...");
                Scanner scanr = new Scanner(System.in);
                scanr.nextLine(); // Waits for the user to press Enter
                System.out.println("Continuing...");
                // Call Admin profile page
                AdminPage(scanner, result);
            }
        }
        
    }

    public void ViewProfile(Scanner scanner, String obj) throws IOException, InterruptedException {
        System.out.println(obj);
        System.out.println("Press Enter to continue...");
        Scanner scanr = new Scanner(System.in);
        scanr.nextLine(); // Waits for the user to press Enter
        System.out.println("Continuing...");
        PatientPage(scanner, obj);
        
    }

    public void CompleteRegistration(Scanner scanner, String uid, String userObject) throws IOException, InterruptedException {
        // Clear screen 
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println(("-").repeat(100));
        System.out.println((" ").repeat(40) + "Complete Registration");
        System.out.println(("-").repeat(100));
        
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
            System.out.print("Please Enter your First Name: ");
            patient.firstname = scanner.nextLine();
            System.out.print("Please Enter your Last Name: ");
            patient.lastname = scanner.nextLine();
            System.out.print("Please Enter your Date of Birth in the format DDMMYYYY: ");
            patient.dob = scanner.nextLine();
            
            System.out.print("Have you tested positive for HIV? Y / N: ");
            String hivStatus = scanner.nextLine();
            switch(hivStatus.toUpperCase()){
                case "Y":
                    System.out.print("What date did you receive your results in the format DDMMYYYY: ");
                    patient.diagnosticdate = scanner.nextLine();
                    System.out.print("Are you on Anti Retroviral Treatment? Y / N: ");
                    String arvstatus = scanner.nextLine();
                    switch(arvstatus.toUpperCase()){
                        case "Y":
                            System.out.print("What date did you start your treatment in the format DDMMYYYY: ");
                            patient.artdate = scanner.nextLine();
                            break;
                        case "N":
                            patient.artdate = "NULL";
                            break;
                        default:
                            System.out.print("Invalid Input");
                            System.exit(0);
                    }
                    break;
                case "N":
                    patient.diagnosticdate = "NULL";
                    patient.artdate = "NULL";
                    break;
                default:
                    System.out.print("Invalid Input");
                    System.exit(0);
            }
            
            
            System.out.print(String.format("Update your country of Residence %s: ", patientObj[10]));
            patient.country = scanner.nextLine();

            // Validate country
            while(CheckCountry(patient.country).length() < 5){
                System.out.println("Invalid Country Value " + patient.country);
                System.out.print("Update your country of Residence: ");
                patient.country = scanner.nextLine();
            }

            // Calculate time left to live
            if(patient.diagnosticdate.contains("NULL")){
                //Get Avg Lifespan from country list
                String CtryObj = CheckCountry(patient.country);
                String [] CtryObjArray = CtryObj.split(",");
                float averageLifespan = Float.parseFloat(CtryObjArray[6]);
                patient.yltl = String.valueOf(Math.round(averageLifespan));
            } else {
                patient.yltl = String.valueOf(TimeLeft(patient.country, patient.dob, patient.artdate, patient.diagnosticdate));
            }            

            System.out.print("Press Enter to set a new password");
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
        // Clear screen 
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println(("-").repeat(100));
        System.out.println((" ").repeat(40) + "Profile Update");
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
        System.out.print(String.format("Please Update your First Name %s: ", patientObj[5]));
        patient.firstname = scanner.nextLine();
        System.out.print(String.format("Please Update your Last Name %s: ", patientObj[6]));
        patient.lastname = scanner.nextLine();
        System.out.print(String.format("Please Update your Date of Birth %s in the format DDMMYYYY: ", patientObj[7]));
        patient.dob = scanner.nextLine();
        
        System.out.print(String.format("Update your diagnosis Date %s in the format DDMMYYYY: ", patientObj[8]));
        patient.diagnosticdate = scanner.nextLine();
        System.out.print(String.format("Update the date you begain ART Treatment %s in the format DDMMYYYY: ", patientObj[9]));
        patient.artdate = scanner.nextLine();
        System.out.print("Did you test positive for HIV? Y / N: ");
        String hivStatus = scanner.nextLine();
        switch(hivStatus.toUpperCase()){
            case "Y":
                System.out.print(String.format("Update your diagnosis Date %s in the format DDMMYYYY: ", patientObj[8]));
                patient.diagnosticdate = scanner.nextLine();

                System.out.print("Are you on Anti Retroviral Treatment? Y / N: ");
                String arvstatus = scanner.nextLine();
                switch(arvstatus.toUpperCase()){
                    case "Y":
                        System.out.print("What date did you start your treatment in the format DDMMYYYY: ");
                        patient.artdate = scanner.nextLine();
                        break;
                    case "N":
                        patient.artdate = "NULL";
                        break;
                    default:
                        System.out.print("Invalid Input");
                        System.exit(0);
                }
                        break;
            case "N":
                patient.diagnosticdate = "NULL";
                patient.artdate = "NULL";
                break;
            default:
                System.out.print("Invalid Input");
                System.exit(0);
        }

        
        
        System.out.print(String.format("Update your country of Residence %s: ", patientObj[10]));
        patient.country = scanner.nextLine();
        // Validate country
        while(CheckCountry(patient.country).length() < 5){
            System.out.print("Invalid Country Value " + patient.country);
            System.out.print("Update your country of Residence: ");
            patient.country = scanner.nextLine();
        }

        // Calculate time left to live
        if(patient.diagnosticdate.contains("NULL")){
            //Get Avg Lifespan from country list
            String CtryObj = CheckCountry(patient.country);
            String [] CtryObjArray = CtryObj.split(",");
            float averageLifespan = Float.parseFloat(CtryObjArray[6]);
            patient.yltl = String.valueOf(Math.round(averageLifespan));
        } else {
            patient.yltl = String.valueOf(TimeLeft(patient.country, patient.dob, patient.artdate, patient.diagnosticdate));
        }
        
        //Replace this line in file
        ProcessBuilder pb1 = new ProcessBuilder().redirectErrorStream(true);        
        pb1.command("./update_user.sh", patient.uid, patient.email, patient.password, patient.role, patient.registrationcomplete, patient.firstname, patient.lastname, patient.dob, patient.diagnosticdate, 
            patient.artdate, patient.country, patient.yltl);   
        
        Process p1 = pb1.start();
        String procresult = new String(p1.getInputStream().readAllBytes());
        
        if(procresult.contains("0")){
            //success
            System.out.print("User Updated");
            PatientPage(scanner, obj);
        } else {                
            System.out.print("ERROR User Update Failed");
            // Call Admin profile page
            PatientPage(scanner, obj);
        }
    }

    public void ViewUsers(Scanner scanner, String userObj) throws IOException, InterruptedException{
        try{
            ProcessBuilder pb = new ProcessBuilder();
            pb.command("./viewusers.sh");
            Process p = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            String result = builder.toString();
            System.out.println(result); // Output the collected lines
            System.out.println("End of script execution");
        } catch(IOException e){
            System.out.println("Error executing the script");
            e.printStackTrace();
        }
        
        //String result = new String(p.getInputStream().readAllBytes());
        //System.out.println(result);
        System.out.println("Press Enter to continue...");
        Scanner scanr = new Scanner(System.in);
        scanr.nextLine(); // Waits for the user to press Enter
        System.out.println("Continuing...");

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
                System.out.println("User Data exported Successfully to exported_data.csv at Project Root.");
                System.out.println("Press Enter to continue...");
                Scanner scanr = new Scanner(System.in);
                scanr.nextLine(); // Waits for the user to press Enter
                System.out.println("Continuing...");
                AdminPage(scanner, userObj);
            } else {
                System.out.println("Error in Data Export.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
