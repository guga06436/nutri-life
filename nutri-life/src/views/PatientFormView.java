package views;

import controller.PatientManager;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.impl.PatientManagerImpl;
import model.Patient;
import persistence.db.exception.InfraException;
import service.status.ErrorApplicationStatus;
import service.viewobserver.ViewSubject;

public class PatientFormView extends ViewSubject
{

    private static PatientManager manager;

    /*lida com registro dos pacientes*/
    public PatientFormView(){
        try {
            manager = new PatientManagerImpl();
		} catch (InfraException e) {
            Application.showMessage("Jeez! We noticed an error with our infrastructure. Please try again later."); // Melhorar tratamento
            Application.exitApplication(new ErrorApplicationStatus());
		}
    }

    public void run() {

        boolean running = true;
        while(running){
            Application.showMessage("[1] Sign In");
            Application.showMessage("[2] Exit");
            Application.showMessage("Choose an option: ", false);
            int option = Application.readIntegerInput();
            Application.readLineInput();

            switch (option) {
                case 1:
                    notifyObservers("called signIn()");
                    signIn();
                    break;
                case 2:
                    notifyObservers("exiting view");
                    Application.showMessage("Exiting...");
                    running = false;
                    break;
                default:
                    Application.showMessage("Invalid option");
            }
        }
    }

    private static void signIn() {

        Application.showMessage("Login: ", false);
        String login = Application.readLineInput();

        Application.showMessage("Password: ", false);
        String password = Application.readLineInput();

        try {
            Patient loggedInPatient = manager.retrieve(login, password);
            Application.showMessage("Login successful for patient " + loggedInPatient.getName());
        } catch (EntityNotFoundException e) {
            Application.showMessage("Login Failed: " + e.getMessage());
        } catch (InfraException e) {
            Application.showMessage("Error with our database, please come again after we fix it.");
        }
    }
}
