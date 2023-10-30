package views;

import controller.MealPlanManager;
import controller.exceptions.DeleteException;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.exceptions.UpdateException;
import controller.impl.MealPlanManagerImpl;
import handlers.OptionHandler;
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
            System.out.println("Jeez! We noticed an error with our infrastructure. Please try again later.");
            System.exit(1);
        }
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("[1] Create Meal Plan");
            System.out.println("[2] View Meal Plan");
            System.out.println("[3] Edit Meal Plan");
            System.out.println("[4] Remove Meal Plan");
            System.out.println("[5] Exit");
            System.out.print("Choose an option: ");
            int option = OptionHandler.readIntegerInput();
            OptionHandler.readLineInput();

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
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void removeMealPlan() {

        MealPlan mealPlan;
        try {
            mealPlan = manager.retrieve(patient);
        } catch (EntityNotFoundException e) {
            System.out.println("There is no Meal Plan for this patient, please create one");
            return;
        } catch (InfraException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.print("Are you sure to delete? [Y/N]: ");
        String option;
        do {
            option = OptionHandler.readStringInput().toUpperCase();
        } while (!option.equals("Y") && !option.equals("N"));

        if (option.equals("Y")) {
            try {
                manager.deleteMealPlan(mealPlan);
            } catch (DeleteException e) {
                System.out.println(e.getMessage());
            } catch (InfraException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createMealPlan() {
        System.out.println("Create a New Meal Plan:");

        System.out.print("Plan Name: ");
        String planName = OptionHandler.readStringInput();

        System.out.print("Goals: ");
        String goals = OptionHandler.readStringInput();

        // You will need to handle the creation of meals and recipeList here as per your application's requirements.

        // Create a new MealPlan instance
        try {
            manager.createMealPlan(planName, goals, null, null, patient, nutritionist);
            System.out.println("Creation successful");
        } catch (RegisterException e) {
            System.out.println(e.getMessage());
        } catch (InfraException e) {
            System.out.println(e.getMessage());
        }
    }

    private void viewMealPlan() {
        try {
            MealPlan mealPlan = manager.retrieve(patient);
            System.out.println(mealPlan.getPlanName());
            System.out.println(mealPlan.getGoals());
            System.out.println(mealPlan.getCreationDate().toString());
            System.out.println(mealPlan.getMeals());
            System.out.println(mealPlan.getRecipeList());
        } catch (InfraException e) {
            System.out.println(e.getMessage());
        } catch (EntityNotFoundException e) {
            System.out.println("There is no Meal Plan for this patient, please create one");
        }
    }

    private void updatePlanName(MealPlan mealplan) {
        System.out.println("Plan Name: ");
        String name = OptionHandler.readStringInput();
        try {
            manager.updateMealPlan(mealplan, name, mealplan.getGoals(), mealplan.getMeals(), mealplan.getRecipeList());
            System.out.println("Plan Name updated successfully.");
        } catch (UpdateException e) {
            System.out.println(e.getMessage());
        } catch (InfraException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateGoal(MealPlan mealplan) {
        System.out.println("Goal: ");
        String goal = OptionHandler.readStringInput();
        try {
            manager.updateMealPlan(mealplan, mealplan.getPlanName(), goal, mealplan.getMeals(), mealplan.getRecipeList());
            System.out.println("Goal updated successfully.");
        } catch (UpdateException e) {
            System.out.println(e.getMessage());
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
                System.out.println("Editing Meal Plan: ");
                viewMealPlan();
            } catch (InfraException e) {
                System.out.println(e.getMessage());
                break;
            } catch (EntityNotFoundException e) {
                System.out.println("There is no Meal Plan for this patient, please create one");
                break;
            }

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
                    System.out.println("Invalid option");
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