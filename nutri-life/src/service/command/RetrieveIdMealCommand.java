package service.command;

import controller.MealManager;
import model.Meal;
import service.MealCommand;

public class RetrieveIdMealCommand implements MealCommand<Integer>{
	private MealManager manager;
	
	public RetrieveIdMealCommand(MealManager manager) {
		this.manager = manager;
	}

	@Override
	public Integer execute(Meal meal) throws Exception {
		return manager.retrieveId(meal);
	}
}
