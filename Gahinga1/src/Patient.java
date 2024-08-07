import java.io.IOException;

public class Patient extends User {
    private String dateOfBirth;
    private String diagnosisDate;
    private boolean onART;
    private String artStartDate;
    private String countryCode;
    private int yearsLeftToLive;

    // Getters and Setters...

    public void viewProfile() {
        System.out.println("First Name: " + this.firstName);
        System.out.println("Last Name: " + this.lastName);
        System.out.println("Date of Birth: " + this.dateOfBirth);
        System.out.println("Diagnosis Date: " + this.diagnosisDate);
        System.out.println("On ART: " + (this.onART ? "Yes" : "No"));
        System.out.println("ART Start Date: " + this.artStartDate);
        System.out.println("Country Code: " + this.countryCode);
        System.out.println("Years Left to Live: " + this.yearsLeftToLive);
    }

    public void updateProfile(String dateOfBirth, String diagnosisDate, boolean onART, String artStartDate, String countryCode) {
        this.dateOfBirth = dateOfBirth;
        this.diagnosisDate = diagnosisDate;
        this.onART = onART;
        this.artStartDate = artStartDate;
        this.countryCode = countryCode;

        ProcessBuilder pb = new ProcessBuilder();
        pb.command("bash", "-c", String.format("./update_user.sh %s %s %s %s %s %s", this.email, dateOfBirth, diagnosisDate, onART ? "1" : "0", artStartDate, countryCode));
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Error updating profile.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
