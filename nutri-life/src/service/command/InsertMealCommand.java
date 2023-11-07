package service.command;

import java.util.List;

import controller.MealManager;
import controller.impl.MealManagerImpl;
import model.Meal;
import persistence.db.exception.InfraException;
import service.MealCommand;

public class InsertMealCommand implements MealCommand<Boolean>{
	private MealManager manager;
	
	public InsertMealCommand() throws InfraException {
		try {
			this.manager = new MealManagerImpl();
		} catch (InfraException e) {
			throw e;
		}
	}

	@Override
	public Boolean execute(List<Meal> objects) throws Exception {
		return manager.insert(objects.get(0));
	}
}
