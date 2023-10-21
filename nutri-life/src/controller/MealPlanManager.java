package controller;

import java.util.List;

import controller.exceptions.DeleteException;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.exceptions.UpdateException;
import model.Meal;
import model.MealPlan;
import model.Nutritionist;
import model.Patient;
import model.Recipe;

public interface MealPlanManager {
    void createMealPlan(String planName, String goals,List<Meal> meals,  List<Recipe> recipeList, Patient patient, Nutritionist nutritionist) throws RegisterException;
    void updateMealPlan(MealPlan mealPlan, String planName, String goals, List<Recipe> recipeList) throws UpdateException;
    void deleteMealPlan(MealPlan mealPLan) throws DeleteException;
    void retrieve(Patient patient) throws EntityNotFoundException;
}
