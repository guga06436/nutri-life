package service.command;

import java.util.List;

import controller.MealManager;
import model.Meal;
import service.MealCommand;

public class RetrieveMealCommand implements MealCommand<List<Meal>>{
	private MealManager manager;
	
	public RetrieveMealCommand(MealManager manager) {
		this.manager = manager;
	}
	
	@Override
	public List<Meal> execute(Meal meal) throws Exception {
		return manager.retrieve(meal);
	}
}
