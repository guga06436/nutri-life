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
import persistence.db.exception.InfraException;

public interface MealPlanManager {
    void createMealPlan(String planName, String goals, List<Meal> meals, Patient patient, Nutritionist nutritionist) throws RegisterException, InfraException;
    void updateMealPlan(MealPlan mealPlan, String planName, String goals, List<Meal> meals) throws UpdateException, InfraException;
    void deleteMealPlan(MealPlan mealPLan) throws DeleteException, InfraException;
    MealPlan retrieve(Patient patient) throws EntityNotFoundException, InfraException;
}
