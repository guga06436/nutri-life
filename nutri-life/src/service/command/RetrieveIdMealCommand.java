package service.command;

import java.util.List;

import controller.MealManager;
import controller.impl.MealManagerImpl;
import model.Meal;
import persistence.db.exception.InfraException;
import service.MealCommand;

public class RetrieveIdMealCommand implements MealCommand<Integer>{
	private MealManager manager;
	
	public RetrieveIdMealCommand() throws InfraException {
		try {
			this.manager = new MealManagerImpl();
		} catch (InfraException e) {
			throw e;
		}
	}

	@Override
	public Integer execute(List<Meal> objects) throws Exception {
		return manager.retrieveId(objects.get(0));
	}
}
