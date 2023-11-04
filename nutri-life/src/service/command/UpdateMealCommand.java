package service.command;

import controller.MealManager;
import model.Meal;
import service.MealCommand;

public class UpdateMealCommand implements MealCommand<Boolean>{
	private MealManager manager;
	
	public UpdateMealCommand(MealManager manager) {
		this.manager = manager;
	}

	@Override
	public Boolean execute(Meal meal) throws Exception {
		return manager.updateMeal(meal);
	}

}
