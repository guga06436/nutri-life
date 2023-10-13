package views;

import controller.exceptions.ExceptionLogin;
import controller.exceptions.ExceptionRegister;
import handlers.OptionHandler;
import model.Nutritionist;
import persistence.db.exception.InfraException;
import service.Facade;

public class NutritionistFormView {

    private Facade manager;

    public NutritionistFormView() {
        try {
			this.manager = Facade.getInstance();
		} catch (InfraException e) {
			System.out.println("Jeez! We noticed an error with our infrastructure. Please try again later."); // Melhorar tratamento
            e.printStackTrace();
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
                    break;
            }
        }
    }

    public void register() {

        System.out.print("Name: ");
        String name = OptionHandler.readLineInput();

        System.out.print("Age: ");
        int age = OptionHandler.readIntegerInput();

        System.out.print("CRN: ");
        String crn = OptionHandler.readStringInput();

        System.out.print("Username: ");
        String username = OptionHandler.readStringInput();

        System.out.print("Password: ");
        String password = OptionHandler.readStringInput();

        boolean registerSuccess = false;
		try {
			registerSuccess = this.manager.addNutritionist(name, age, crn, username, password);
		} catch (InfraException e) {
			System.out.println(e.getMessage()); // Melhorar Tratamento
		} catch (ExceptionRegister e) {
            System.out.println(e.getMessage());
        }

        if (registerSuccess) {
            System.out.println("Registration successful for nutritionist.");
        } else {
            System.out.println("Registration failed for nutritionist.");
        }
    }

    public void signIn() {

        System.out.print("Username: ");
        String username = OptionHandler.readStringInput();

        System.out.print("Password: ");
        String password = OptionHandler.readStringInput();

        try {
            Nutritionist loggedInNutritionist = this.manager.retrieveNutritionist(username, password);
            System.out.println("Login successful for nutritionist: " + loggedInNutritionist.getName());
        } catch (ExceptionLogin e) {
            System.out.println("Login Failed: " + e.getMessage());
        } catch (InfraException e) {
            System.out.println("Error with our database, please come again after we fix it.");
        }
    }

}