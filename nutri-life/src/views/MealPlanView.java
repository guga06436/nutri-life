package views;

import controller.exceptions.ExceptionMealPlan;
import handlers.OptionHandler;
import model.Nutritionist;
import model.Patient;
import persistence.db.exception.InfraException;
import service.Facade;

public class MealPlanView {

    private Facade manager;
    Patient patient;

    public MealPlanView(Patient patient) {
        // Initialize your MealPlanManager here, for example:
        try {
            manager = Facade.getInstance();
            this.patient = patient;
        } catch (InfraException e) {
            System.out.println("Jeez! We noticed an error with our infrastructure. Please try again later."); // Melhorar tratamento
            System.exit(1);
        }
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("[1] View Meal Plan");
            System.out.println("[2] Edit Meal Plan");
            System.out.println("[3] Exit");
            System.out.print("Choose an option: ");
            int option = OptionHandler.readIntegerInput();
            OptionHandler.readLineInput();

            switch (option) {
                case 1:
                    viewMealPlan();
                    break;
                case 2:
                    editMealPlan();
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

    private void viewMealPlan() {

        try {
            manager.viewMealPlan(patient);
        } catch (ExceptionMealPlan e) {
            System.out.println(e.getMessage());
        } catch (InfraException e) {
            System.out.println(e.getMessage());
        }
    }

    private void editMealPlan() {
        boolean editing = true;

        while (editing) {

            try {
                MealPlan mealplan = manager.getMealPlan(patient);
                System.out.println("Editing Meal Plan: " + manager.getMealPlanName(mealplan));
                manager.getRecipes(mealplan);
            } catch (InfraException e) {
                System.out.println(e.getMessage());
            } catch (ExceptionMealPlan e) {
                System.out.println(e.getMessage());
            }

            // Prompt the user to select an action for the recipes
            System.out.println("[1] Edit Plan Name");
            System.out.println("[2] Edit Goals");
            System.out.println("[3] Add Recipe");
            System.out.println("[4] Remove Recipe");
            System.out.println("[5] Back to Main Menu");
            System.out.print("Choose an option: ");
            int option = OptionHandler.readIntegerInput();
            OptionHandler.readLineInput();

            switch (option) {
                case 1:
                    System.out.println("Plan Name: ");
                    String name = OptionHandler.readStringInput();
                    manager.setMealPlanName(name);
                case 2:
                    System.out.println("Goal: ");
                    String goal = OptionHandler.readStringInput();
                    manager.setMealPlanGoal(goal);
                case 3:
                    addRecipe();
                    break;
                case 4:
                    removeRecipe();
                    break;
                case 5:
                    editing = false; // Exit the loop and go back to the main menu
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
}
