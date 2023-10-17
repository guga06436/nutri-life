package views;

import controller.PatientManager;
import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import controller.exceptions.ExceptionRegister;
import controller.impl.PatientManagerImpl;
import handlers.OptionHandler;
import model.Patient;
import persistence.db.exception.InfraException;

public class PatientFormView {

    private static PatientManager manager;

    /*lida com registro dos pacientes*/
    public PatientFormView(){
        try {
            manager = new PatientManagerImpl();
		} catch (InfraException e) {
            System.out.println("Jeez! We noticed an error with our infrastructure. Please try again later."); // Melhorar tratamento
            System.exit(1);
		}
    }

    public void run() {

        boolean running = true;
        while(running){
            System.out.println("[1] Sign In");
            System.out.println("[2] Register");
            System.out.println("[3] Exit");
            System.out.print("Choose an option: ");
            int option = OptionHandler.readIntegerInput();
            OptionHandler.readLineInput();

            switch (option) {
                case 1:
                    signIn();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private static void signIn() {

        System.out.print("Login: ");
        String login = OptionHandler.readLineInput();

        System.out.print("Password: ");
        String password = OptionHandler.readLineInput();

        try {
            Patient loggedInPatient = manager.retrieve(login, password);
            System.out.println("Login successful for patient" + loggedInPatient.getName());
        } catch (ExceptionNotFound | ExceptionPassword e) {
            System.out.println("Login Failed: " + e.getMessage());
        } catch (InfraException e) {
            System.out.println("Error with our database, please come again after we fix it.");
        }
    }

    private static void register() {

        System.out.print("Name: ");
        String name = OptionHandler.readLineInput();

        System.out.print("Age: ");
        int age = OptionHandler.readIntegerInput();
        OptionHandler.readLineInput();
        
        System.out.print("CPF: ");
        String cpf = OptionHandler.readStringInput();

        System.out.print("Height: ");
        float height = OptionHandler.readFloatInput();

        System.out.print("Weight: ");
        float weight = OptionHandler.readFloatInput();
        OptionHandler.readLineInput();

        System.out.print("Username: ");
        String username = OptionHandler.readStringInput();

        System.out.print("Password: ");
        String password = OptionHandler.readStringInput();

        boolean registerSuccess = false;
		try {
			registerSuccess = manager.add(username , password, name, cpf, age, height, weight);
		} catch (InfraException e) {
			System.out.println("Error with our database detected.");
		} catch (ExceptionRegister e) {
            System.out.println(e.getMessage());
        }

        if (registerSuccess) {
            System.out.println("Registration successful for patient " + name);
        } else {
            System.out.println("Registration failed for patient.");
        }
    }

}
