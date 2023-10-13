package views;

import controller.exceptions.ExceptionLogin;
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
            this.manager = Facade.getInstance();
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
            Patient loggedInPatient = manager.retrievePatient(login, password);
            System.out.println("Login successful for patient" + loggedInPatient.getName());
        } catch (InfraException e) {
            System.out.println(e.getMessage()); // Melhorar Tratamento
        } catch (ExceptionLogin e) {
            System.out.println(e.getMessage());
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
			registerSuccess = manager.addPatient(username , password, name, cpf, age, height, weight);
		} catch (InfraException e) {
			System.out.println("Error with our database detected.");
		} catch (ExceptionRegister e) {
            System.out.println(e.getMessage());
        }

        /*Condição de Sucesso*/
        if (registerSuccess) {
            System.out.println("Success" + name);
        } else {
            System.out.println("Failed");
        }
    }

}
