package views;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.exceptions.RegisterException;
import model.Food;
import model.Meal;
import model.MealPlan;
import persistence.db.exception.InfraException;
import service.MealCommand;
import service.command.DeleteMealCommand;
import service.command.InsertMealCommand;
import service.command.ListAllMealCommand;
import service.command.RestoreMealCommand;
import service.command.RetrieveByIdMealCommand;
import service.command.RetrieveIdMealCommand;
import service.command.RetrieveMealCommand;
import service.command.UpdateMealCommand;
import service.viewobserver.ViewSubject;

public class MealFormView extends ViewSubject
{
    private MealPlan mealPlan;
    private List<Meal> meals;
    private HashMap<String, MealCommand> cmds = new HashMap<>();

    public MealFormView(MealPlan mealPlan) throws InfraException {
    	this.mealPlan = mealPlan;
    	initCommands();
    }
    
    private void initCommands() throws InfraException {
    	cmds.put("retrieve", new RetrieveMealCommand());
    	cmds.put("insert", new InsertMealCommand());
    	cmds.put("update", new UpdateMealCommand());
    	cmds.put("delete", new DeleteMealCommand());
    	cmds.put("listAll", new ListAllMealCommand());
    	cmds.put("retrieveId", new RetrieveIdMealCommand());
    	cmds.put("retrieveById", new RetrieveByIdMealCommand());
    	cmds.put("restore", new RestoreMealCommand());
    }

    public void run() throws Exception {
        boolean running = true;
        while (running) {
            Application.showMessage("[1] Create Meal");
            Application.showMessage("[2] View Meals");
            Application.showMessage("[3] Edit Meal");
            Application.showMessage("[4] Remove Meal");
            Application.showMessage("[5] Restaurar Meal");
            Application.showMessage("[6] Back to Meal Plan");
            Application.showMessage("Choose an option: ", false);
            int option = Application.readIntegerInput();
            Application.readLineInput();

            switch (option) {
                case 1:
                    notifyObservers("exiting insert()");
                    insert();
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
                	notifyObservers("called restoreMeal()");
                	restoreMeal();
                	break;
                case 6:
                    notifyObservers("exiting view");
                    Application.showMessage("Returning to Meal Plan...");
                    running = false;
                    break;
                default:
                    Application.showMessage("Invalid option", false);
            }
        }
    }

    private void insert() throws Exception {
        Application.showMessage("Create a New Meal:");

        Application.showMessage("Name: ", false);
        String name = Application.readStringInput();
        Application.showMessage("Time: ", false);
        String time = Application.readStringInput();
        
        //FALTA PEGAR OS VALORES DE PORTIONEDFOODS DO BANCO DE DADOS
        Map<Food, Map<Float, String>> portionedFoods = null;

        Meal newMeal = new Meal(name, time, portionedFoods, mealPlan);

        try {
			@SuppressWarnings("unchecked")
			MealCommand<Boolean> cmd = (MealCommand<Boolean>)cmds.get("insert");
            cmd.execute(Arrays.asList(newMeal));
            meals.add(newMeal);
            Application.showMessage("Meal created successfully.");
        } catch (RegisterException e) {
            Application.showMessage("Error: " + e.getMessage());
        }
    }

    private void viewMeals() {
        Application.showMessage("Meals in the Meal Plan:");
        for (Meal meal : mealPlan.getMeals()) {
            Application.showMessage(meal.getName() + " (" + meal.getTime() + ")");
        }
    }

    private void editMeal() {
        System.out.print("Enter the name of the meal to edit: ");
        String mealName = Application.readStringInput();

        Meal mealToEdit = null;
        for (Meal meal : mealPlan.getMeals()) {
            if (meal.getName().equals(mealName)) {
                mealToEdit = meal;
                break;
            }
        }

        if (mealToEdit == null) {
            Application.showMessage("Meal not found.");
            return;
        }

        Application.showMessage("New name (leave empty to keep the same): ", false);
        String newName = Application.readStringInput();
        Application.showMessage("New time (leave empty to keep the same): ", false);
        String newTime = Application.readStringInput();
        
        Meal newMeal = new Meal(mealToEdit);

        if (!newName.isEmpty()) {
        	mealToEdit.setName(newName);
        }
        if (!newTime.isEmpty()) {
        	mealToEdit.setTime(newTime);
        }

        try {
			@SuppressWarnings("unchecked")
			MealCommand<Boolean> cmd = (MealCommand<Boolean>)cmds.get("update");
            cmd.execute(Arrays.asList(mealToEdit, newMeal));
            Application.showMessage("Meal updated successfully.");
        } 
        catch(Exception e) {
        	Application.showMessage("Error: " + e.getMessage());
        }
    }

    private void removeMeal() {
        Application.showMessage("Enter the name of the meal to remove: ", false);
        String mealName = Application.readStringInput();

        Meal mealToRemove = null;
        for (Meal meal : mealPlan.getMeals()) {
            if (meal.getName().equals(mealName)) {
                mealToRemove = meal;
                break;
            }
        }

        if (mealToRemove == null) {
            Application.showMessage("Meal not found.");
            return;
        }

        try {
			@SuppressWarnings("unchecked")
			MealCommand<Boolean> cmd = (MealCommand<Boolean>)cmds.get("delete");
            cmd.execute(Arrays.asList(mealToRemove));
            mealPlan.getMeals().remove(mealToRemove);
            
            if(meals.contains(mealToRemove)) {
            	meals.remove(mealToRemove);
            }
            Application.showMessage("Meal removed successfully.");
        }
        catch(Exception e) {
        	Application.showMessage("Error: " + e.getMessage());
        }
    }
    
    private void restoreMeal() {
        System.out.print("Enter the name of the meal to edit: ");
        String mealName = Application.readStringInput();

        Meal mealToRestore = null;
        for (Meal meal : mealPlan.getMeals()) {
            if (meal.getName().equals(mealName)) {
                mealToRestore = meal;
                break;
            }
        }

        if (mealToRestore == null) {
            Application.showMessage("Meal not found.");
            return;
        }

        try {
			@SuppressWarnings("unchecked")
			MealCommand<Boolean> cmd = (MealCommand<Boolean>)cmds.get("restore");
            cmd.execute(Arrays.asList(mealToRestore));
            Application.showMessage("Meal restored successfully.");
        } 
        catch(Exception e) {
        	Application.showMessage("Error: " + e.getMessage());
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