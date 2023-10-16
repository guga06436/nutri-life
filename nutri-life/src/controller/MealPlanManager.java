package controller;

import controller.exceptions.ExceptionMealPlan;
import model.Patient;

public interface MealPlanManager {
    void createMealPlan(String planName, String goals, List<Recipe> recipeList) throws ExceptionMealPlan;
    void updateMealPlan(MealPlan mealPlan, String planName, String goals, List<Recipe> recipeList) throws ExceptionMealPlan;
    void deleteMealPlan(MealPLan mealPLan) throws ExceptionMealPlan;
    void viewMealPlan(Patient patient);

}