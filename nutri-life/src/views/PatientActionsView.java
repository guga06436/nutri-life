package views;

import controller.PatientManager;
import controller.impl.PatientManagerImpl;
import model.Patient;
import persistence.db.exception.InfraException;
import service.status.ErrorApplicationStatus;
import service.viewobserver.ViewSubject;

public class PatientActionsView extends ViewSubject
{
    private PatientManager manager;
    private Patient loggedInPatient;

    public PatientActionsView(Patient loggedInPatient) {
        try {
            this.manager = new PatientManagerImpl();
        } catch (InfraException e) {
            Application.showMessage("Jeez! We noticed an error with our infrastructure. Please try again later.");
            Application.exitApplication(new ErrorApplicationStatus());
        }

        this.loggedInPatient = loggedInPatient;
    }

    public void run() {
        notifyObservers("called run()");

        boolean running = true;
        while (running) {
            Application.showMessage("Welcome, " + loggedInPatient.getName() + "!");
            Application.showMessage("[1] View Meal Plan");
            Application.showMessage("[2] Log out");
            Application.showMessage("Choose an option: ");
            int option = Application.readIntegerInput();
            Application.readLineInput();

            switch (option) {
                case 1:
                    notifyObservers("called viewMealPlan()");
                    viewMealPlan();
                    break;
                case 2:
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

    private void viewMealPlan() {
        MealPlanPatientView mealPlanPatientView;
		try {
			mealPlanPatientView = new MealPlanPatientView(loggedInPatient, manager.retrievePatientNutritionist(loggedInPatient));
			mealPlanPatientView.viewMealPlan();
		} catch (InfraException e) {
			Application.showMessage(e.getMessage());
		}
    }

}