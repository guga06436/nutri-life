package service.command;

import java.util.List;

import controller.MealManager;
import controller.impl.MealManagerImpl;
import model.Meal;
import persistence.db.exception.InfraException;
import service.MealCommand;

public class UpdateMealCommand implements MealCommand<Boolean>{
	private MealManager manager;
	
	public UpdateMealCommand() throws InfraException {
		try {
			this.manager = new MealManagerImpl();
		} catch (InfraException e) {
			throw e;
		}
	}

	@Override
	public Boolean execute(List<Meal> objects) throws Exception {
		Meal originalMeal = objects.get(0);
		Meal updatedMeal = objects.get(1);
		
		return manager.updateMeal(originalMeal, updatedMeal);
	}

}
