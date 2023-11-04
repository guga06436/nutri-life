package service.command;

import java.util.List;

import controller.MealManager;
import model.Meal;
import persistence.db.exception.InfraException;
import service.MealCommand;

public class ListAllMealCommand implements MealCommand<List<Meal>>{
	private MealManager manager;
	
	public ListAllMealCommand(MealManager manager) {
		this.manager = manager;
	}

	@Override
	public List<Meal> execute(Meal meal) throws InfraException {
		return manager.listAll();
	}
}
