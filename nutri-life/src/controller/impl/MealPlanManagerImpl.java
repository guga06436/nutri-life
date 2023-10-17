package controller.impl;

import java.util.Date;
import java.util.List;

import controller.MealPlanManager;
import controller.exceptions.ExceptionMealPlan;
import lombok.Data;
import model.MealPlan;
import model.Patient;
import model.Recipe;
import persistence.Persistence;
import persistence.db.exception.InfraException;
import persistence.impl.FactoryMealPlan;

@Data
public class MealPlanManagerImpl implements MealPlanManager{
	private static FactoryMealPlan fmp;
	private static Persistence<MealPlan> persistence;
	
	public MealPlanManagerImpl() throws InfraException {
		fmp = new FactoryMealPlan();
		persistence = fmp.getPersistence();
	}
	
	@Override
	public void createMealPlan(String planName, String goals, List<Recipe> recipeList) throws ExceptionMealPlan {
		MealPlan mp = new MealPlan(planName, new Date(), goals, recipeList);
	}

	@Override
	public void updateMealPlan(MealPlan mealPlan, String planName, String goals, List<Recipe> recipeList)
			throws ExceptionMealPlan {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMealPlan(MealPlan mealPLan) throws ExceptionMealPlan {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void viewMealPlan(Patient patient) {
		// TODO Auto-generated method stub
		
	}
}