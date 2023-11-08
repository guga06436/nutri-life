package views;

import controller.MealPlanManager;
import controller.exceptions.EntityNotFoundException;
import controller.impl.MealPlanManagerImpl;
import model.MealPlan;
import model.Nutritionist;
import model.Patient;
import persistence.db.exception.InfraException;
import service.status.ErrorApplicationStatus;
import service.viewobserver.ViewSubject;

public class MealPlanPatientView extends ViewSubject {

    private MealPlanManager manager;
    private Patient patient;
    private Nutritionist nutritionist;

    public MealPlanPatientView(Patient patient, Nutritionist nutritionist) {
        try {
            manager = new MealPlanManagerImpl();
            this.patient = patient;
            this.nutritionist = nutritionist;
        } catch (InfraException e) {
            Application.showMessage("Jeez! We noticed an error with our infrastructure. Please try again later.");
            Application.exitApplication(new ErrorApplicationStatus());
        }
    }

    public void viewMealPlan() {
        try {
            MealPlan mealPlan = manager.retrieve(patient);
            Application.showMessage(mealPlan.getPlanName());
            Application.showMessage(mealPlan.getGoals());
            Application.showMessage(mealPlan.getCreationDate().toString());
            Application.showMessage(mealPlan.getMeals());
        } catch (InfraException e) {
            Application.showMessage(e.getMessage());
        } catch (EntityNotFoundException e) {
            Application.showMessage("There is no Meal Plan for this patient, please create one");
        }
    }
}