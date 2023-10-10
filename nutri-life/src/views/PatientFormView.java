package views;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.exceptions.ExceptionLogin;
import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import controller.exceptions.ExceptionRegister;
import handlers.OptionHandler;
import model.Patient;
import persistence.db.exception.InfraException;
import service.Facade;

public class PatientFormView {

    private static Facade manager;

    /*lida com registro dos pacientes*/
    public PatientFormView(){
        try {
            manager = new Facade();
		} catch (InfraException e) {
            System.out.println("Jeez! We noticed an error with our infrastructure. Please try again later."); // Melhorar tratamento
		}
    }

    public void run() throws ExceptionRegister {

        OptionHandler sc = new OptionHandler();

        while(true){
            System.out.println("[1] Sign In");
            System.out.println("[2] Register");
            System.out.println("[3] Exit");
            System.out.print("Choose an option: ");
            int option = sc.readIntegerInput();
            sc.readLineInput();

            switch (option) {
                case 1:
                    try {
                        signIn(sc);
                    } catch (ExceptionLogin | ExceptionPassword e) {
                        System.out.println("Login failed: " + e.getMessage());
                    } catch (ExceptionNotFound e){
                        System.out.println("Login failed: Patient not found");
                    }
                case 2:
                    register(sc);
                case 3:
                    System.out.println("Exiting...");
                    sc.onExitProgram();
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private static void signIn(OptionHandler scanner) throws ExceptionLogin, ExceptionPassword, ExceptionNotFound {

        System.out.print("Login: ");
        String login = scanner.readLineInput();

        System.out.print("Password: ");
        String password = scanner.readLineInput();

        try {
            Patient loggedInPatient = manager.retrievePatient(login, password);
            System.out.println("Login successful for patient" + loggedInPatient.getName());
        } catch (ExceptionNotFound e) {
            System.out.println("Login Failed: " + e.getMessage());
        } catch (ExceptionPassword e) {
            System.out.println("Password Failed: " + e.getMessage());
        }
    }

    private static void register(OptionHandler scanner) throws ExceptionRegister {

        System.out.print("Name: ");
        String name = scanner.readLineInput();

        System.out.print("Age: ");
        int age = scanner.readIntegerInput();
        scanner.readLineInput();
        
        System.out.print("CPF: ");
        String cpf = scanner.readStringInput();

        System.out.print("Height: ");
        float height = scanner.readFloatInput();

        System.out.print("Weight: ");
        float weight = scanner.readFloatInput();
        scanner.readLineInput();

        System.out.print("Username: ");
        String username = scanner.readStringInput();

        System.out.print("Password: ");
        String password = scanner.readStringInput();

        if (password.length() < 8 || password.length() > 20) {
            throw new ExceptionRegister("Password lenght must be between 8 and 20.");
        }
        
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(password);
        
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        
        if(count < 2) {
        	throw new ExceptionRegister("The password must have at least 2 numbers");
        }
        
        boolean containsLetters = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                containsLetters = true;
                break; // Se encontrar uma letra, sai do loop
            }
        }

        if (!containsLetters) {
            throw new ExceptionRegister("The password msut have at least 1 letter");
        } 

        Patient newPatient = new Patient(username, password, name, cpf, age, height, weight);
        boolean registerSuccess = false;
		try {
			registerSuccess = manager.addPatient(newPatient);
		} catch (InfraException e) {
			System.out.println(e.getMessage());
		}

        /*Condição de Sucesso*/
        if (registerSuccess) {
            System.out.println("Success" + newPatient.getName());
        } else {
            System.out.println("Failed" + newPatient.getName());
        }

        if (username.length() > 12) {
            throw new ExceptionRegister("Login must not be of length above 12.");
        } else if (username.isEmpty()) {
            throw new ExceptionRegister("Login must not be empty.");
        } else if (username.matches(".*\\d.*")) {
            throw new ExceptionRegister("Login must not contain numbers.");
        }
    }

}
