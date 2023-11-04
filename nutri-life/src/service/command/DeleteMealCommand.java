package service.command;

import controller.MealManager;
import model.Meal;
import service.MealCommand;

public class DeleteMealCommand implements MealCommand<Boolean>{
	private MealManager manager;
	
	public DeleteMealCommand(MealManager manager) {
		this.manager = manager;
	}

	@Override
	public Boolean execute(Meal meal) throws Exception {
		manager.deleteMeal(meal);
		
		return true;
	}
}
