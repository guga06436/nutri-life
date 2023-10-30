package views;

import controller.MealPlanManager;
import controller.exceptions.DeleteException;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.exceptions.UpdateException;
import controller.impl.MealPlanManagerImpl;
import service.Application;
import model.MealPlan;
import model.Nutritionist;
import model.Patient;
import persistence.db.exception.InfraException;
import service.viewobserver.ViewSubject;

public class MealPlanView extends ViewSubject
{

    private MealPlanManager manager;
    private Patient patient;
    private Nutritionist nutritionist;

    public MealPlanView(Patient patient, Nutritionist nutritionist) {
        try {
            manager = new MealPlanManagerImpl();
            this.patient = patient;
            this.nutritionist = nutritionist;
        } catch (InfraException e) {
            Application.showMessage("Jeez! We noticed an error with our infrastructure. Please try again later.");
            Application.exitApplication(1);
        }
    }

    public void run() {
        boolean running = true;
        while (running) {
            Application.showMessage("[1] Create Meal Plan");
            Application.showMessage("[2] View Meal Plan");
            Application.showMessage("[3] Edit Meal Plan");
            Application.showMessage("[4] Remove Meal Plan");
            Application.showMessage("[5] Exit");
            Application.showMessage("Choose an option: ", false);
            int option = Application.readIntegerInput();
            Application.readLineInput();

            switch (option) {
                case 1:
                    createMealPlan();
                    break;
                case 2:
                    notifyObservers("called editMealPlan()");
                    viewMealPlan();
                    break;
                case 3:
                    notifyObservers("called editMealPlan()");
                    editMealPlan();
                    break;
                case 4:
                    notifyObservers("called removeMealPlan()");
                    removeMealPlan();
                case 5:
                    notifyObservers("exiting view");
                    Application.showMessage("Exiting...");
                    running = false;
                    break;
                default:
                    Application.showMessage("Invalid option");
            }
        }
    }

    private void removeMealPlan() {

        MealPlan mealPlan;
        try {
            mealPlan = manager.retrieve(patient);
        } catch (EntityNotFoundException e) {
            Application.showMessage("There is no Meal Plan for this patient, please create one");
            return;
        } catch (InfraException e) {
            Application.showMessage(e.getMessage());
            return;
        }

        System.out.print("Are you sure to delete? [Y/N]: ");
        String option;
        do {
            option = Application.readStringInput().toUpperCase();
        } while (!option.equals("Y") && !option.equals("N"));

        if (option.equals("Y")) {
            try {
                manager.deleteMealPlan(mealPlan);
            } catch (DeleteException e) {
                Application.showMessage(e.getMessage());
            } catch (InfraException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createMealPlan() {
        Application.showMessage("Create a New Meal Plan:");

        Application.showMessage("Plan Name: ", false);
        String planName = Application.readStringInput();

        Application.showMessage("Goals: ", false);
        String goals = Application.readStringInput();

        // You will need to handle the creation of meals and recipeList here as per your application's requirements.

        // Create a new MealPlan instance
        try {
            manager.createMealPlan(planName, goals, null, null, patient, nutritionist);
            Application.showMessage("Creation successful");
        } catch (RegisterException e) {
            Application.showMessage(e.getMessage());
        } catch (InfraException e) {
            Application.showMessage(e.getMessage());
        }
    }

    private void viewMealPlan() {
        try {
            MealPlan mealPlan = manager.retrieve(patient);
            Application.showMessage(mealPlan.getPlanName());
            Application.showMessage(mealPlan.getGoals());
            Application.showMessage(mealPlan.getCreationDate().toString());
            Application.showMessage(mealPlan.getMeals());
            Application.showMessage(mealPlan.getRecipeList());
        } catch (InfraException e) {
            Application.showMessage(e.getMessage());
        } catch (EntityNotFoundException e) {
            Application.showMessage("There is no Meal Plan for this patient, please create one");
        }
    }

    private void updatePlanName(MealPlan mealplan) {
        Application.showMessage("Plan Name: ");
        String name = Application.readStringInput();
        try {
            manager.updateMealPlan(mealplan, name, mealplan.getGoals(), mealplan.getMeals(), mealplan.getRecipeList());
            Application.showMessage("Plan Name updated successfully.");
        } catch (UpdateException e) {
            Application.showMessage(e.getMessage());
        } catch (InfraException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateGoal(MealPlan mealplan) {
        Application.showMessage("Goal: ");
        String goal = Application.readStringInput();
        try {
            manager.updateMealPlan(mealplan, mealplan.getPlanName(), goal, mealplan.getMeals(), mealplan.getRecipeList());
            Application.showMessage("Goal updated successfully.");
        } catch (UpdateException e) {
            Application.showMessage(e.getMessage());
        } catch (InfraException e) {
            throw new RuntimeException(e);
        }
    }

    private void editMealPlan() {
        boolean editing = true;
        MealPlan mealplan;

        while (editing) {
            try {
                mealplan = manager.retrieve(patient);
                Application.showMessage("Editing Meal Plan: ");
                viewMealPlan();
            } catch (InfraException e) {
                Application.showMessage(e.getMessage());
                break;
            } catch (EntityNotFoundException e) {
                Application.showMessage("There is no Meal Plan for this patient, please create one");
                break;
            }

            Application.showMessage("[1] Edit Plan Name");
            Application.showMessage("[2] Edit Goals");
            Application.showMessage("[3] Add Recipe");
            Application.showMessage("[4] Remove Recipe");
            Application.showMessage("[5] Back to Main Menu");
            Application.showMessage("Choose an option: ", false);
            int option = Application.readIntegerInput();
            Application.readLineInput();

            switch (option) {
                case 1:
                    updatePlanName(mealplan);
                    break;
                case 2:
                    updateGoal(mealplan);
                    break;
                case 3:
                    //addRecipe();
                    break;
                case 4:
                    //removeRecipe();
                    break;
                case 5:
                    editing = false;
                    break;
                default:
                    Application.showMessage("Invalid option");
            }
        }
    }
/*
    private void addRecipe() {


        System.out.println("Adding Recipe");
        System.out.print("Recipe Name: ");
        String recipeName = OptionHandler.readStringInput();

        Recipe newRecipe = new Recipe(recipeName, ingredients, instructions);


        manager.addRecipeToMealPlan(newRecipe, patient);

        System.out.println("Recipe added to the meal plan.");
    }

    private void removeRecipe() {


        System.out.println("Removing Recipe");

        MealPlan mealPlan = manager.getMealPlan(patient);
        List<Recipe> recipes = mealPlan.getRecipes();

        if (recipes.isEmpty()) {
            System.out.println("No recipes in the meal plan to remove.");
            return;
        }

        System.out.println("Recipes in the meal plan:");
        for (int i = 0; i < recipes.size(); i++) {
            System.out.println((i + 1) + ". " + recipes.get(i).getName());
        }

        System.out.print("Enter the number of the recipe to remove: ");
        int recipeIndex = OptionHandler.readIntegerInput();

        if (recipeIndex >= 1 && recipeIndex <= recipes.size()) {

            Recipe removedRecipe = recipes.remove(recipeIndex - 1);
            manager.setRecipes(mealPlan, recipes);
            System.out.println(removedRecipe.getName() + " removed from the meal plan.");
        } else {
            System.out.println("Invalid recipe selection.");
        }
    }
*/
}