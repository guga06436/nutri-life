package controler.impl;

import controler.NutritionistManager;
import exceptions.ExceptionNotFound;
import exceptions.ExceptionPassword;
import model.Nutritionist;
import persistence.NutritionistPersistence;

public class NutritionistManagerImpl implements NutritionistManager{
	private NutritionistPersistence np;
	
	public NutritionistManagerImpl() {
		np = new NutritionistPersistence();
	}
	
	@Override
	public boolean add(Nutritionist n) {
		return n.add(n);
	}

    @Override
    public void listAll() {
        for(Nutritionist n: NutritionistPersistence.nutritionistPersistence) {
            System.out.println(n);
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