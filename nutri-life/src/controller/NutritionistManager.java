package controller;

import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import model.Nutritionist;
import persistence.db.exception.InfraException;

public interface NutritionistManager {
	boolean add(Nutritionist n) throws InfraException;

	Nutritionist retrieve(String login, String password) throws ExceptionNotFound, ExceptionPassword;
}