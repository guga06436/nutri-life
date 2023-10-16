package controller;

import java.util.List;

import controller.exceptions.ExceptionMealPlan;
import model.MealPlan;
import model.Patient;
import model.Recipe;

public interface MealPlanManager {
    void createMealPlan(String planName, String goals, List<Recipe> recipeList) throws ExceptionMealPlan;
    void updateMealPlan(MealPlan mealPlan, String planName, String goals, List<Recipe> recipeList) throws ExceptionMealPlan;
    void deleteMealPlan(MealPlan mealPLan) throws ExceptionMealPlan;
    void viewMealPlan(Patient patient);
}
