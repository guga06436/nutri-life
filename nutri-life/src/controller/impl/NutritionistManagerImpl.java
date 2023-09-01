package controller.impl;

import controller.NutritionistManager;
import controller.exceptions.DatabaseException;
import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import model.Nutritionist;
import persistence.NutritionistPersistence;
import persistence.db.exception.InfraException;

public class NutritionistManagerImpl implements NutritionistManager{
	private NutritionistPersistence np;
	
	public NutritionistManagerImpl() {
		try {
			np = new NutritionistPersistence();
		}
		catch(InfraException e) {
			throw new DatabaseException("Could not connect to the database."); 
		}
	}
	
	@Override
	public boolean add(Nutritionist n) {
		try {
			return np.add(n);
		}
		catch(InfraException e) {
			throw new DatabaseException("Unable to create a nutritionist.");
		}
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