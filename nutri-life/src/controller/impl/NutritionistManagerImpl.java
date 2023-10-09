package controller.impl;

import controller.NutritionistManager;
import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import model.Nutritionist;
import persistence.NutritionistPersistence;
import persistence.db.exception.InfraException;

public class NutritionistManagerImpl implements NutritionistManager{
	private NutritionistPersistence np;
	
	public NutritionistManagerImpl() throws InfraException {
		np = new NutritionistPersistence();
	}
	
	@Override
	public boolean add(Nutritionist n) throws InfraException {
		return np.add(n);
	}

	@Override
	public Nutritionist retrieve(String login, String password) throws ExceptionNotFound, ExceptionPassword {
		/* Usado para verificar o login */

		/*if (loggedInNutritionist == null) {
			throw new ExceptionNotFound("Nutritionist not found");
		}

		if (!loggedInNutritionist.getPassword().equals(password)) {
			throw new ExceptionPassword("Invalid password");
		}*/

		return null;
	}
}