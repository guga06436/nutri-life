package controller;

import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import model.Nutritionist;

public interface NutritionistManager {
	boolean add(Nutritionist n);

	Nutritionist retrieve(String login, String password) throws ExceptionNotFound, ExceptionPassword;
}