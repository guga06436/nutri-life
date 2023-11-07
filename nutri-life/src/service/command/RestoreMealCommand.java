package service.command;

import java.util.List;

import controller.MealManager;
import controller.impl.MealManagerImpl;
import model.Meal;
import persistence.db.exception.InfraException;
import service.MealCommand;

public class RestoreMealCommand implements MealCommand<Boolean>{
	private MealManager manager;
	
	public RestoreMealCommand() throws InfraException {
		try {
			this.manager = new MealManagerImpl();
		} catch (InfraException e) {
			throw e;
		}
	}

	@Override
	public Boolean execute(List<Meal> meals) throws Exception {
		manager.restoreMeal(meals.get(0));
		
		return true;
	}

}
