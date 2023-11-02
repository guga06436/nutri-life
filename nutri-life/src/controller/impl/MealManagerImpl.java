package controller.impl;

import java.util.List;
import java.util.Map;

import controller.MealManager;
import controller.exceptions.DeleteException;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.exceptions.UpdateException;
import model.Food;
import model.Meal;
import model.MealPlan;
import persistence.Persistence;
import persistence.db.exception.InfraException;
import persistence.impl.FactoryMeal;
import service.LogService;
import service.impl.LogAdapter;

public class MealManagerImpl implements MealManager {
    private static FactoryMeal mf;
    private static Persistence<Meal> persistence;
    private static final LogService log = LogAdapter.getInstance();

    public MealManagerImpl() throws InfraException {
        try {
            mf = new FactoryMeal();
            persistence = mf.getPersistence();
        }
        catch(InfraException e) {
            log.logException(e);
            throw e;
        }
    }

    @Override
    public boolean insert(String name, Map<Food, Map<Float, String>> portionedIngredients, int hour, int minutes, int seconds, MealPlan mealPlan) throws InfraException, RegisterException {
        try {
            validateTime(hour, minutes, seconds);


            String time = hour + ":" + minutes + ":" + seconds;
            Meal m = new Meal(name, time, portionedIngredients, mealPlan);
            return persistence.insert(m);
        } catch(RegisterException e) {
            log.logException(e);
            throw e;
        } catch(InfraException e) {
            log.logException(e);
            throw e;
        }
    }

    private void validateTime(int hour, int minute, int second) throws RegisterException {
        if (hour < 0 && hour > 23) {
            throw new RegisterException("Hour must be between 0 and 23.");
        }

        if (minute < 0 && minute > 60) {
            throw new RegisterException("Minute must be between 0 and 60.");
        }

        if (second < 0 && second > 60) {
            throw new RegisterException("Second must be between 0 and 60.");
        }
    }

    @Override
    public void updateMeal(Meal meal, String name, Map<Food, Map<Float, String>> portionedIngredients, int hour, int minutes, int seconds) throws UpdateException {
        // TODO Auto-generated method stub
    }

    @Override
    public void deleteMeal(Meal meal) throws DeleteException {
        // TODO Auto-generated method stub
    }

    @Override
    public List<Meal> retrieve(MealPlan mealPlan) throws EntityNotFoundException, IllegalArgumentException, InfraException {

        try {
            validateMealPlan(mealPlan);

            /*
            Meal m = new Meal();
            List<Meal> mealList = persistence.retrieve(m, mealPlan);
            if (mealList == null || mealList.size() == 0) {
                String message = "No meal found";

                log.logDebug(message + " [meals: " + mealList + "] ");
                throw new EntityNotFoundException(message);
            }
            return mealList;
            */
        } catch (IllegalArgumentException e) {
            log.logException(e);
            throw e;
        }
		return null;
    }

    private void validateMealPlan(MealPlan mealPlan) throws IllegalArgumentException {
        if (mealPlan == null) {
            throw new IllegalArgumentException("Meal plan cannot be null");
        }

        if (isEmptyString(mealPlan.getPlanName())) {
            throw new IllegalArgumentException("Meal plan name cannot be empty");
        }
        // You can add more attribute checks here as needed.
    }

    private static boolean isEmptyString(String str) {
        return str == null || str.trim().isEmpty();
    }
}
