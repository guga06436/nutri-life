package controler;

import exceptions.ExceptionNotFound;
import exceptions.ExceptionPassword;
import model.Nutritionist;

public interface NutritionistManager {
	boolean add(Nutritionist n);

	Nutritionist retrieve(String login, String password) throws ExceptionNotFound, ExceptionPassword;
}