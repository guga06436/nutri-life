package controller;

import java.util.List;

import controller.exceptions.DeleteException;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.exceptions.UpdateException;
import model.Meal;
import persistence.db.exception.InfraException;

public interface MealManager {

    boolean insert(Meal meal) throws InfraException, RegisterException;
    boolean updateMeal(Meal originalMeal, Meal updatedMeal) throws UpdateException;
    void deleteMeal(Meal meal) throws DeleteException;
    List<Meal> retrieve(Meal meal) throws EntityNotFoundException, InfraException;
    int retrieveId(Meal meal);
    Meal retrieveById(int id);
    List<Meal> listAll();
    void restoreMeal(Meal meal);
}
