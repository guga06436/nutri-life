package controller;

import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import controller.exceptions.ExceptionRegister;
import model.Nutritionist;
import persistence.db.exception.InfraException;

public interface NutritionistManager {
	boolean add(String name, int age, String crn, String username, String password) throws InfraException, ExceptionRegister;

	Nutritionist retrieve(String username, String password) throws ExceptionNotFound, ExceptionPassword, InfraException;
}