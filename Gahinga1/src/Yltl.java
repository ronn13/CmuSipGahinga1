
import java.io.IOException;

public class Yltl {

    static void CheckUuid(String uniqueid) throws IOException{
        //
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("/mnt/c/Users/STUDENT/SchoolWork/ProgBootcamp/CmuSipGahinga1/Gahinga1/src/sample.sh", uniqueid);
        Process p = pb.start();
        String result = new String(p.getInputStream().readAllBytes());
        System.out.println(result);
    }

    //In class assignment about polymorphism
    public abstract class Vehicle {
        int numberOfTyres;
        String colour;

        public abstract void IsLicensed();
        
        public void Transmission(){
            System.out.println("Changes gears");
        }
        
        public void Fuel(String fueltype){
            System.out.print("Has %s fuel" + fueltype);
        }
    }

    public class Car extends Vehicle{

        //implement abstract method
        public void IsLicensed() {
            System.out.println("Car is licensed");
        }

        //method overriding
        public void Transmission(String trantype) {
            System.out.println("Has %s transmission" + trantype);
        }

    }

    public static void main(String[] args) throws Exception {
        Car bmw = new Car();
        bmw.colour = "red";        
        
    }
    
}
