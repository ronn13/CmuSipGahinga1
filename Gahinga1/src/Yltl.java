import java.io.IOException;
import java.util.Scanner;

public class Yltl {   

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Register User by entering the user's email: ");
        String email = scanner.nextLine();
        //If email does not already exist then create user
        String result = CheckUserFile(email);        
        if (result.length()> 1) {
            //Email exists
            System.out.println("User Already exists");
            // Call landing page
            //AdminPage(scanner);
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
            String uid = "004";

            ProcessBuilder pb = new ProcessBuilder();
            pb.command("./add_user.sh", uid, email, "NULL", "patient", "0", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL");            
            Process p = pb.start();

            String procresult = new String(p.getInputStream().readAllBytes());
            //check exit type of processbuilder rather than check count of result
            if(procresult.contains("0")){
                //success
                System.out.println("New User created with uid " + uid);
            } else {                
                System.out.println("ERROR User Creation Failed");
            }
            //002    janedoe@gmail.com   pa55word    patient    0    Jane   Doe    01012000    01012013    01012014    KES    YearsLeftToLive
        }

        
    }

    static String CheckUserFile(String uniqueid) throws IOException{
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("./sample.sh", uniqueid);
        Process p = pb.start();
        String result = new String(p.getInputStream().readAllBytes());
        return(result);
    }
    
}
