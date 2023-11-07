package service.command;

import java.util.List;

import controller.MealManager;
import controller.impl.MealManagerImpl;
import model.Meal;
import persistence.db.exception.InfraException;
import service.MealCommand;

public class RetrieveMealCommand implements MealCommand<List<Meal>>{
	private MealManager manager;
	
	public RetrieveMealCommand() throws InfraException {
		try {
			this.manager = new MealManagerImpl();
		} catch (InfraException e) {
			throw e;
		}
	}
	
	@Override
	public List<Meal> execute(List<Meal> objects) throws Exception {
		return manager.retrieve(objects.get(0));
	}
}
