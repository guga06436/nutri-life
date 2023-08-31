package views;

import model.Patient;
import exceptions.ExceptionLogin;
import exceptions.ExceptionPassword;
import exceptions.ExceptionNotFound;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class PatientFormView {

    /*Lista de armazenamento = melhorar o método de armazenamento*/
    private static List<Patient> patients = new ArrayList<>();

    public void () {

        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("[1] Sign In");
            System.out.println("[2] Register");
            System.out.println("[3] Exit");
            System.out.print("Choose an option: ");
            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    try {
                        signIn(sc);
                    } catch (ExceptionLogin | ExceptionPassword e) {
                        System.out.println("Login failed: " + e.getMessage());
                    } catch (ExceptionNotFound e){
                        System.out.println("Login failed: Patient not found");
                    }
                    break;
                case 2:
                    register(sc);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option");
            }
        }
    }


    private static void signIn(Scanner scanner) throws ExceptionLogin, ExceptionPassword, ExceptionNotFound {

        System.out.print("Login: ");
        String login = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Patient loggedInPatient = findPatientByLogin(login);

        if (loggedInPatient == null) {
            throw new ExceptionNotFound("Patient not found");
        }

        if (!loggedInPatient.getPassword().equals(password)) {
            throw new ExceptionPassword("Invalid password");
        }

        System.out.println("Login successful for patient: " + loggedInPatient.getName());
    }

    /* Busca de paciente existente*/
    private static Patient findPatientByLogin(String login) {
        for (Patient patient : patients) {
            if (patient.getLogin().equals(login)) {
                return patient;
            }
        }
        return null;
    }

    private static Date parseBirthdate(String birthdate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.parse(birthdate);
        } catch (ParseException e) {
            return null;
        }
    }

    private static void register(Scanner scanner) {

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Birthdate: ");
        String birthdate = scanner.nextLine();

        // Convert String to Date
        Date date = parseBirthdate(birthdate);
        if (date == null) {
            System.out.println("Invalid birthdate format");
            return;
        }

        System.out.print("Height: ");
        float height = scanner.nextFloat();

        System.out.print("Weight: ");
        float weight = scanner.nextFloat();
        scanner.nextLine();

        System.out.print("Login: ");
        String login = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Patient newPatient = new Patient(login, password, name, age, date, height, weight);
        patients.add(newPatient);

        System.out.println("Registration successful for patient: " + newPatient.getName());

        if (login.length() > 12) {
            throw new ExceptionRegister("Login must not be of length above 12.");
        } else if (login.isEmpty()) {
            throw new ExceptionRegister("Login must not be empty.");
        } else if (login.matches(".*\\d.*")) {
            throw new ExceptionRegister("Login must not contain numbers.");
        }

        System.out.print("Password: ");
        String password = sc.next();
        if (password.length() < 8 || password.length() > 20) {
            throw new ExceptionRegister("Password lenght must be between 8 and 20.");
        }
    }

}
