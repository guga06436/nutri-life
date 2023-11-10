package views;

import controller.MealPlanManager;
import controller.NutritionistManager;
import controller.PatientManager;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.exceptions.UpdateException;
import controller.impl.MealPlanManagerImpl;
import controller.impl.NutritionistManagerImpl;
import controller.impl.PatientManagerImpl;
import model.MealPlan;
import model.Nutritionist;
import model.Patient;
import persistence.db.exception.InfraException;
import service.iterators.Iterator;
import service.iterators.ListIterator;
import service.status.ErrorApplicationStatus;
import service.viewobserver.ViewSubject;

import java.util.List;

public class NutritionistActionsView extends ViewSubject {
    private NutritionistManager manager;
    private Nutritionist loggedInNutritionist;

    public NutritionistActionsView(Nutritionist loggedInNutritionist) {
        try {
            this.manager = new NutritionistManagerImpl();
        } catch (InfraException e) {
            Application.showMessage("Jeez! We noticed an error with our infrastructure. Please try again later.");
            Application.exitApplication(new ErrorApplicationStatus());
        }

        this.loggedInNutritionist = loggedInNutritionist;
    }

    public void run() {
        notifyObservers("called run()");

        boolean running = true;
        while (running) {
            Application.showMessage("Welcome, " + loggedInNutritionist.getName() + "!");
            Application.showMessage("[1] Create Patient");
            Application.showMessage("[2] View Patients");
            Application.showMessage("[3] Edit Patient Meal Plan");
            Application.showMessage("[4] Log out");
            Application.showMessage("Choose an option: ");
            int option = Application.readIntegerInput();
            Application.readLineInput();

            switch (option) {
                case 1:
                    notifyObservers("called createPatient()");
                    createPatient();
                    break;
                case 2:
                    notifyObservers("called viewPatients()");
                    viewPatients();
                    break;
                case 3:
                    notifyObservers("called editMealPlan()");
                    editMealPlan();
                    break;
                case 4:
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

    private void viewPatients() {
        List<Patient> patientList = null;
        try {
            patientList = manager.listAllPatients(this.loggedInNutritionist);
        } catch (InfraException e) {
            Application.showMessage(e.getMessage());
            return;
        }
        if (patientList.isEmpty()) {
            Application.showMessage("There is no patient");
        } else {
            Iterator<Patient> iterator = new ListIterator<>(patientList);
            while (iterator.hasNext()) {
                Patient patient = iterator.next();
                Application.showMessage((iterator.getIndex()) + patient.getName());
            }
        }
    }

    private void editMealPlan() {

        viewPatients();
        
        List<Patient> patientList = null;
        int selection = -1;
        
        try {
	        patientList = manager.listAllPatients(loggedInNutritionist);
	        while (selection < 1 || selection > patientList.size()) {
	            Application.showMessage("Enter the number of the Patient you want to see: ", false);
	            selection = Application.readIntegerInput();
	        }
        }
        catch(InfraException e) {
        	Application.showMessage(e.getMessage());
        }

        MealPlanView mealPlanView = new MealPlanView(patientList.get(selection-1), loggedInNutritionist);
        mealPlanView.run();
    }


    private void createPatient() {

        PatientManager managerPatient = null;
        try {
            managerPatient = new PatientManagerImpl();
        } catch (InfraException e) {
            Application.showMessage(e.getMessage());
            Application.exitApplication(new ErrorApplicationStatus());
        }

        Application.showMessage("Name: ", false);
        String name = Application.readLineInput();

        Application.showMessage("Age: ", false);
        int age = Application.readIntegerInput();
        Application.readLineInput();

        Application.showMessage("CPF: ", false);
        String cpf = Application.readStringInput();

        Application.showMessage("Height: ", false);
        float height = Application.readFloatInput();

        Application.showMessage("Weight: ", false);
        float weight = Application.readFloatInput();
        Application.readLineInput();

        Application.showMessage("Username: ", false);
        String username = Application.readStringInput();

        Application.showMessage("Password: ", false);
        String password = Application.readStringInput();

        boolean registerSuccess = false;
        try {
            registerSuccess = managerPatient.add(username , password, name, cpf, age, height, weight);
            
            if(registerSuccess) {
            	manager.updatePatients(loggedInNutritionist, new Patient(username, password, name, cpf, age, height, weight));
            }
        } catch (InfraException e) {
            Application.showMessage("Error with our database detected.");
        } catch (RegisterException e) {
            Application.showMessage(e.getMessage());
        }
        catch(UpdateException e) {
        	Application.showMessage(e.getMessage());
        }

    }
}
