package views;

import controller.NutritionistManager;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.impl.NutritionistManagerImpl;
import service.Application;
import model.Nutritionist;
import persistence.db.exception.InfraException;
import service.status.ErrorApplicationStatus;
import service.viewobserver.ViewSubject;

public class NutritionistFormView extends ViewSubject
{
    private NutritionistManager manager;

    public NutritionistFormView() {
        try {
			manager = new NutritionistManagerImpl();
		} catch (InfraException e) {
            Application.showMessage("Jeez! We noticed an error with our infrastructure. Please try again later."); // Melhorar tratamento
            Application.exitApplication(new ErrorApplicationStatus());
		}
    }

    public void run() {

        boolean running = true;
        while(running){
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
                    Application.showMessage("Invalid option");
                    break;
            }
        }
    }

    public void register() {

        Application.showMessage("Name: ");
        String name = Application.readLineInput();

        Application.showMessage("Age: ");
        int age = Application.readIntegerInput();

        Application.showMessage("CRN: ");
        String crn = Application.readStringInput();

        Application.showMessage("Username: ");
        String username = Application.readStringInput();

        Application.showMessage("Password: ");
        String password = Application.readStringInput();

        boolean registerSuccess = false;
		try {
			registerSuccess = this.manager.add(name, age, crn, username, password);
		} catch (InfraException e) {
			Application.showMessage(e.getMessage()); // Melhorar Tratamento
		} catch (RegisterException e) {
            Application.showMessage(e.getMessage());
        }

        if (registerSuccess) {
            Application.showMessage("Registration successful for nutritionist " + name);
        } else {
            Application.showMessage("Registration failed for nutritionist.");
        }
    }

    public void signIn() {

        Application.showMessage("Username: ", false);
        String username = Application.readStringInput();

        Application.showMessage("Password: ", false);
        String password = Application.readStringInput();

        try {
            Nutritionist loggedInNutritionist = this.manager.retrieve(username, password);
            Application.showMessage("Login successful for nutritionist: " + loggedInNutritionist.getName());
        } catch (EntityNotFoundException e) {
            Application.showMessage("Login Failed: " + e.getMessage());
        } catch (InfraException e) {
            Application.showMessage("Error with our database, please come again after we fix it.");
        }
    }

}