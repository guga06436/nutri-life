package views;

import controller.MealManager;
import controller.exceptions.CreateException;
import controller.exceptions.DeleteException;
import controller.exceptions.UpdateException;
import model.Meal;
import model.MealPlan;
import model.Food;
import persistence.db.exception.InfraException;
import handlers.OptionHandler;
import service.viewobserver.ViewSubject;

public class MealFormView extends ViewSubject
{
    private MealManager mealManager;
    private MealPlan mealPlan;

    public MealFormView(MealPlan mealPlan) {
        this.mealManager = new MealManager();
        this.mealPlan = mealPlan;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("[1] Create Meal");
            System.out.println("[2] View Meals");
            System.out.println("[3] Edit Meal");
            System.out.println("[4] Remove Meal");
            System.out.println("[5] Back to Meal Plan");
            System.out.print("Choose an option: ");
            int option = OptionHandler.readIntegerInput();
            OptionHandler.readLineInput();

            switch (option) {
                case 1:
                    notifyObservers("exiting createMeal()");
                    createMeal();
                    break;
                case 2:
                    notifyObservers("called viewMeals()");
                    viewMeals();
                    break;
                case 3:
                    notifyObservers("called editMeal()");
                    editMeal();
                    break;
                case 4:
                    notifyObservers("called removeMeal()");
                    removeMeal();
                    break;
                case 5:
                    notifyObservers("exiting view");
                    System.out.println("Returning to Meal Plan...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void createMeal() {
        System.out.println("Create a New Meal:");

        System.out.print("Name: ");
        String name = OptionHandler.readStringInput();
        System.out.print("Time: ");
        String time = OptionHandler.readStringInput();


        Meal newMeal = new Meal(name, time, portionedFoods, mealPlan);

        try {
            mealManager.createMeal(newMeal);
            System.out.println("Meal created successfully.");
        } catch (CreateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewMeals() {
        System.out.println("Meals in the Meal Plan:");
        for (Meal meal : mealPlan.getMeals()) {
            System.out.println(meal.getName() + " (" + meal.getTime() + ")");
        }
    }

    private void editMeal() {
        System.out.print("Enter the name of the meal to edit: ");
        String mealName = OptionHandler.readStringInput();

        Meal mealToEdit = null;
        for (Meal meal : mealPlan.getMeals()) {
            if (meal.getName().equals(mealName)) {
                mealToEdit = meal;
                break;
            }
        }

        if (mealToEdit == null) {
            System.out.println("Meal not found.");
            return;
        }

        System.out.print("New name (leave empty to keep the same): ");
        String newName = OptionHandler.readStringInput();
        System.out.print("New time (leave empty to keep the same): ");
        String newTime = OptionHandler.readStringInput();

        if (!newName.isEmpty()) {
            mealToEdit.setName(newName);
        }
        if (!newTime.isEmpty()) {
            mealToEdit.setTime(newTime);
        }

        try {
            mealManager.updateMeal(mealToEdit);
            System.out.println("Meal updated successfully.");
        } catch (UpdateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeMeal() {
        System.out.print("Enter the name of the meal to remove: ");
        String mealName = OptionHandler.readStringInput();

        Meal mealToRemove = null;
        for (Meal meal : mealPlan.getMeals()) {
            if (meal.getName().equals(mealName)) {
                mealToRemove = meal;
                break;
            }
        }

        if (mealToRemove == null) {
            System.out.println("Meal not found.");
            return;
        }

        try {
            mealManager.deleteMeal(mealToRemove);
            System.out.println("Meal removed successfully.");
        } catch (DeleteException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    /* Lógicas adcionais de food->meal

    private void addFoodToMeal(Meal meal) {
        System.out.print("Enter the name of the food: ");
        String foodName = OptionHandler.readStringInput();


        Food food = findFoodByName(foodName);

        if (food == null) {
            System.out.println("Food not found.");
            return;
        }

        System.out.print("Enter the portion: ");
        float portion = OptionHandler.readFloatInput();
        System.out.print("Enter a description for the portion: ");
        String portionDescription = OptionHandler.readStringInput();


        meal.addFoodWithPortion(food, portion, portionDescription);
    }

    private void editFoodInMeal(Meal meal) {
        System.out.print("Enter the name of the food to edit: ");
        String foodName = OptionHandler.readStringInput();


        Food food = findFoodByName(foodName);

        if (food == null) {
            System.out.println("Food not found.");
            return;
        }

        if (meal.getFoods().contains(food)) {
            System.out.print("Enter the new portion: ");
            float newPortion = OptionHandler.readFloatInput();
            System.out.print("Enter a new description for the portion: ");
            String newPortionDescription = OptionHandler.readStringInput();


            meal.updatePortion(food, newPortion, newPortionDescription);
        } else {
            System.out.println("Food not found in the meal.");
        }
    }

    private void removeFoodFromMeal(Meal meal) {
        System.out.print("Enter the name of the food to remove: ");
        String foodName = OptionHandler.readStringInput();


        Food food = findFoodByName(foodName);

        if (food == null) {
            System.out.println("Food not found.");
            return;
        }

        if (meal.getFoods().contains(food)) {

            meal.removeFood(food);
            System.out.println("Food removed from the meal.");
        } else {
            System.out.println("Food not found in the meal.");
        }
    }
    */

}