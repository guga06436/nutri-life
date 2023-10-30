package views;

import controller.AdminManager;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.impl.AdminManagerImpl;
import handlers.OptionHandler;
import model.Admin;
import persistence.db.exception.InfraException;
import service.viewobserver.ViewSubject;

public class AdminFormView extends ViewSubject
{

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
                    notifyObservers("called signIn()");
                    signIn();
                    break;
                case 2:
                    notifyObservers("called register()");
                    register();
                    break;
                case 3:
                    notifyObservers("exiting view");
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

        System.out.print("Username: ");
        String username = OptionHandler.readStringInput();

        System.out.print("Password: ");
        String password = OptionHandler.readStringInput();

        boolean registerSuccess = false;
        try {
            registerSuccess = adminManager.insert(name, username, password);
        } catch (InfraException e) {
            System.out.println("Error with our database detected.");
        } catch (RegisterException e) {
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
        } catch (EntityNotFoundException e) {
            System.out.println("Login Failed: " + e.getMessage());
        } catch (InfraException e) {
            System.out.println("Error with our database, please come again after we fix it.");
        }
    }
}
