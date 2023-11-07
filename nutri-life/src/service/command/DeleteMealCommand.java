package service.command;

import java.util.List;

import controller.MealManager;
import controller.impl.MealManagerImpl;
import model.Meal;
import persistence.db.exception.InfraException;
import service.MealCommand;

public class DeleteMealCommand implements MealCommand<Boolean>{
	private MealManager manager;
	
	public DeleteMealCommand() throws InfraException {
		try {
			this.manager = new MealManagerImpl();
		} catch (InfraException e) {
			throw e;
		}
	}

	@Override
	public Boolean execute(List<Meal> objects) throws Exception {
		manager.deleteMeal(objects.get(0));
		
		return true;
	}
}
