package views;

import controller.exceptions.ExceptionMealPlan;
import handlers.OptionHandler;
import model.MealPlan;
import model.Nutritionist;
import model.Patient;
import persistence.db.exception.InfraException;
import service.Facade;

public class MealPlanView {

    private Facade manager;
    private Patient patient;

    public MealPlanView(Patient patient) {
        try {
            manager = Facade.getInstance();
            this.patient = patient;
        } catch (InfraException e) {
            System.out.println("Jeez! We noticed an error with our infrastructure. Please try again later.");
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
                    manager.setMealPlanName(name, mealplan);
                    break;
                case 2:
                    System.out.println("Goal: ");
                    String goal = OptionHandler.readStringInput();
                    manager.setMealPlanGoal(goal, mealplan);
                    break;
                case 3:
                    addRecipe();
                    break;
                case 4:
                    removeRecipe();
                    break;
                case 5:
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

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
}