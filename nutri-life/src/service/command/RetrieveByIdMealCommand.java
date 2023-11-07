package service.command;

import java.util.List;

import controller.MealManager;
import controller.impl.MealManagerImpl;
import model.Meal;
import persistence.db.exception.InfraException;
import service.MealCommand;

public class RetrieveByIdMealCommand implements MealCommand<Meal>{
	private MealManager manager;
	
	public RetrieveByIdMealCommand() throws InfraException {
		try {
			this.manager = new MealManagerImpl();
		} catch (InfraException e) {
			throw e;
		}
	}

	@Override
	public Meal execute(List<Meal> objects) throws Exception {
		return manager.retrieveById(manager.retrieveId(objects.get(0)));
	}
}
