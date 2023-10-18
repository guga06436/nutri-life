package controller.impl;

import java.util.Date;
import java.util.List;

import controller.MealPlanManager;
import controller.exceptions.DeleteException;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.exceptions.UpdateException;
import lombok.Data;
import model.MealPlan;
import model.Patient;
import model.Recipe;
import persistence.Persistence;
import persistence.db.exception.InfraException;
import persistence.impl.FactoryMealPlan;
import service.LogService;
import service.impl.LogAdapter;

@Data
public class MealPlanManagerImpl implements MealPlanManager{
	private static final LogService log = new LogAdapter();
	private static FactoryMealPlan fmp;
	private static Persistence<MealPlan> persistence;
	
	public MealPlanManagerImpl() throws InfraException {
		try {
			fmp = new FactoryMealPlan();
			persistence = fmp.getPersistence();
		}
		catch(InfraException e) {
			log.logException(e);
			throw e;
		}
	}
	
	@Override
	public void createMealPlan(String planName, String goals, List<Recipe> recipeList) throws RegisterException {
		MealPlan mp = new MealPlan(planName, new Date(), goals, recipeList);
	}

	@Override
	public void updateMealPlan(MealPlan mealPlan, String planName, String goals, List<Recipe> recipeList) throws UpdateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMealPlan(MealPlan mealPLan) throws DeleteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retrieve(Patient patient) throws EntityNotFoundException{ 
		// TODO Auto-generated method stub
		
	}
}