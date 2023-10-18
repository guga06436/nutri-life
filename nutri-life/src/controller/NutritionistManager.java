package controller;

import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import model.Nutritionist;
import persistence.db.exception.InfraException;

public interface NutritionistManager {
	boolean add(String name, int age, String crn, String username, String password) throws InfraException, RegisterException;

	Nutritionist retrieve(String username, String password) throws EntityNotFoundException, InfraException;
}