package views;

import controller.AdminManager;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.impl.AdminManagerImpl;
import model.Admin;
import persistence.db.exception.InfraException;
import service.status.ErrorApplicationStatus;
import service.viewobserver.ViewSubject;

public class AdminFormView extends ViewSubject
{

    private AdminManager adminManager;

    public AdminFormView() {
        try {
            adminManager = new AdminManagerImpl();
        } catch (InfraException e) {
            Application.showMessage("Jeez! We noticed an error with our infrastructure. Please try again later.");
            Application.exitApplication(new ErrorApplicationStatus());
        }
    }

    public void run() {
        boolean running = true;
        while (running) {
            Application.showMessage("[1] Sign In");
            Application.showMessage("[2] Register");
            Application.showMessage("[3] Exit");
            Application.showMessage("Choose an option: ", false);
            int option = Application.readIntegerInput();
            Application.readLineInput();

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
                    Application.showMessage("Exiting...");
                    running = false;
                    break;
                default:
                    Application.showMessage("Invalid Option");
                    break;
            }
        }
    }

    private void register() {

        Application.showMessage("Name: ", false);
        String name = Application.readLineInput();

        Application.showMessage("Username: ", false);
        String username = Application.readStringInput();

        Application.showMessage("Password: ", false);
        String password = Application.readStringInput();

        boolean registerSuccess = false;
        try {
            registerSuccess = adminManager.insert(name, username, password);
        } catch (InfraException e) {
            Application.showMessage("Error with our database detected.");
        } catch (RegisterException e) {
            Application.showMessage(e.getMessage());
        }

        if (registerSuccess) {
            Application.showMessage("Registration successful for administrator: " + name);
        } else {
            Application.showMessage("Registration failed for administrator.");
        }
    }

    private void signIn() {
        Application.showMessage("Username: ", false);
        String username = Application.readStringInput();

        Application.showMessage("Password: ", false);
        String password = Application.readStringInput();

        try {
            Admin loggedInAdmin = adminManager.retrieve(username, password);
            Application.showMessage("Login successful for administrator: " + loggedInAdmin.getName());
            AdminActionsView adminActionsView = new AdminActionsView(loggedInAdmin);
            adminActionsView.run();
        } catch (EntityNotFoundException e) {
            Application.showMessage("Login Failed: " + e.getMessage());
        } catch (InfraException e) {
            Application.showMessage("Error with our database, please come again after we fix it.");
        }
    }
}
