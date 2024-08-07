import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Life Prognosis Management Tool.");
        Lifespan.LandingPage(scanner);
        scanner.close();
    }
}
