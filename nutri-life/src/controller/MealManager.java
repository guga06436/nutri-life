package controller;

import controller.exceptions.DeleteException;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.exceptions.UpdateException;
import model.Food;
import model.MealPlan;
import model.Meal;
import persistence.db.exception.InfraException;

import java.sql.Time;
import java.util.List;
import java.util.Map;

public interface MealManager {

    boolean insert(String name, Map<Food, Map<Float, String>> portionedIngredients, int hour, int minutes, int seconds, MealPlan mealPlan) throws InfraException, RegisterException;

    void updateMeal(Meal meal, String name, Map<Food, Map<Float, String>> portionedIngredients, int hour, int minutes, int seconds) throws UpdateException;

    void deleteMeal(Meal meal) throws DeleteException;
    List<Meal> retrieve(MealPlan mealPlan) throws EntityNotFoundException, InfraException;
}
