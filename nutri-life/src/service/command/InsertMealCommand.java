package service.command;

import controller.MealManager;
import model.Meal;
import service.MealCommand;

public class InsertMealCommand implements MealCommand<Boolean>{
	private MealManager manager;
	
	public InsertMealCommand(MealManager manager) {
		this.manager = manager;
	}

	@Override
	public Boolean execute(Meal meal) throws Exception {
		return manager.insert(meal);
	}
}
