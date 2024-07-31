
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Yltl {

    static void CheckUuid(String uniqueid) throws IOException{
        //
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("/mnt/c/Users/STUDENT/SchoolWork/ProgBootcamp/CmuSipGahinga1/Gahinga1/src/sample.sh", uniqueid);
        Process p = pb.start();
        String result = new String(p.getInputStream().readAllBytes());
        System.out.println(result);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to OracleSys!");

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Type 1 To Complete Your Registration. 2: To Login:");
            String menu_selection = br.readLine();

            switch (menu_selection) {
                case "1": 
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));    
                    System.out.println("Enter your User ID:");
                    //search for uid                    
                    String menu_selection1 = br1.readLine();
                    CheckUuid(menu_selection1);
                    break;                                   
                case "2": 
                    System.out.println("2 Selected");
                    //Call Login Method
                default:
                    System.out.println("Invalid Selection " + menu_selection);
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
        
    }
    
}
