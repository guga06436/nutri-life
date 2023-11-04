package service.command;

import controller.MealManager;
import model.Meal;
import service.MealCommand;

public class RetrieveByIdMealCommand implements MealCommand<Meal>{
	private MealManager manager;
	
	public RetrieveByIdMealCommand(MealManager manager) {
		this.manager = manager;
	}

	@Override
	public Meal execute(Meal meal) throws Exception {
		return manager.retrieveById(manager.retrieveId(meal));
	}
}
