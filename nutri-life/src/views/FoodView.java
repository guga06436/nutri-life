package views;

import controller.FoodManager;
import controller.exceptions.EntityNotFoundException;
import controller.impl.FoodManagerImpl;
import model.Food;
import model.enums.FoodGroup;
import persistence.db.exception.InfraException;
import service.iterators.Iterator;
import service.iterators.ListIterator;
import service.status.ErrorApplicationStatus;
import service.viewobserver.ViewSubject;

import java.util.List;

public class FoodView extends ViewSubject {

    private FoodManager manager;

    public FoodView() {
        try {
            manager = new FoodManagerImpl();
        } catch (InfraException e) {
            Application.showMessage("Jeez! We noticed an error with our infrastructure. Please try again later.");
            Application.exitApplication(new ErrorApplicationStatus());
        }
    }

    public Food chooseFood() {

        Application.showMessage("Enter the name of the food to search: ", false);
        String foodName = Application.readStringInput();
        FoodGroup foodGroup = chooseFoodGroup();
        try {
            List<Food> foods = manager.retrieve(foodName, foodGroup);
            return selectionOfFood(foods);
        } catch (EntityNotFoundException e) {
            Application.showMessage("Food not found");
        } catch (InfraException e) {
            Application.showMessage(e.getMessage());
        }

        Application.showMessage("Try to search another Food");
        return chooseFood();
    }

    private FoodGroup chooseFoodGroup() {
        Application.showMessage("Select a Food Group:");
        int index = 1;

        for (FoodGroup group : FoodGroup.values()) {
            Application.showMessage("[" + index + "] " + group.toString());
            index++;
        }

        int selection = -1;
        while (selection < 1 || selection > FoodGroup.values().length) {
            Application.showMessage("Enter the number of the Food Group: ", false);
            selection = Application.readIntegerInput();
        }

        return FoodGroup.values()[selection - 1];
    }

    private Food selectionOfFood(List<Food> foods) {
        Iterator<Food> iterator = new ListIterator<>(foods);
        while (iterator.hasNext()) {
            Application.showMessage((iterator.getIndex() + 1) + " " + iterator.next());
        }

        int selection = -1;
        while (selection < 1 || selection > foods.size()) {
            Application.showMessage("Enter the number of the Food Group: ", false);
            selection = Application.readIntegerInput();
        }
        return foods.get(selection - 1);
    }

}
