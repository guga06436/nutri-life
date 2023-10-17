package views;

import controller.AdminManager;
import controller.exceptions.ExceptionEntityNotFound;
import controller.exceptions.ExceptionPassword;
import controller.exceptions.ExceptionRegister;
import controller.impl.AdminManagerImpl;
import model.Admin;
import handlers.OptionHandler;
import persistence.db.exception.InfraException;

public class AdminFormView {

    private AdminManager adminManager;

    public AdminFormView() {
        try {
            adminManager = new AdminManagerImpl();
        } catch (InfraException e) {
            System.out.println("Jeez! We noticed an error with our infrastructure. Please try again later.");
            System.exit(1);
        }
    }

    public void run() {
        boolean running = true;
        while (running) {
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
                    System.out.println("Invalid Option");
                    break;
            }
        }
    }

    private void register() {

        System.out.print("Name: ");
        String name = OptionHandler.readLineInput();

        System.out.print("Email: ");
        String email = OptionHandler.readStringInput();

        System.out.print("Username: ");
        String username = OptionHandler.readStringInput();

        System.out.print("Password: ");
        String password = OptionHandler.readStringInput();

        boolean registerSuccess = false;
        try {
            registerSuccess = adminManager.insert(name, email, username, password);
        } catch (InfraException e) {
            System.out.println("Error with our database detected.");
        } catch (ExceptionRegister e) {
            System.out.println(e.getMessage());
        }

        if (registerSuccess) {
            System.out.println("Registration successful for administrator: " + name);
        } else {
            System.out.println("Registration failed for administrator.");
        }
    }

    private void signIn() {
        System.out.print("Username: ");
        String username = OptionHandler.readStringInput();

        System.out.print("Password: ");
        String password = OptionHandler.readStringInput();

        try {
            Admin loggedInAdmin = adminManager.retrieve(username, password);
            System.out.println("Login successful for administrator: " + loggedInAdmin.getName());
            //AdminActionsView adminActionsView = new AdminActionsView();
        } catch (ExceptionPassword | ExceptionEntityNotFound e) {
            System.out.println("Login Failed: " + e.getMessage());
        } catch (InfraException e) {
            System.out.println("Error with our database, please come again after we fix it.");
        }
    }
}
