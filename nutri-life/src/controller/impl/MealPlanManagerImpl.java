package controller.impl;

import java.util.Date;
import java.util.List;

import controller.MealPlanManager;
import controller.exceptions.DeleteException;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.exceptions.UpdateException;
import lombok.Data;
import model.Meal;
import model.MealPlan;
import model.Nutritionist;
import model.Patient;
import model.Recipe;
import persistence.Persistence;
import persistence.db.exception.InfraException;
import persistence.impl.FactoryMealPlan;
import service.Application;

@Data
public class MealPlanManagerImpl implements MealPlanManager{
	private static FactoryMealPlan fmp;
	private static Persistence<MealPlan> persistence;
	
	public MealPlanManagerImpl() throws InfraException {
		try {
			fmp = new FactoryMealPlan();
			persistence = fmp.getPersistence();
		}
		catch(InfraException e) {
			Application.logException(e);
			throw e;
		}
	}
	
	@Override
	public void createMealPlan(String planName, String goals,List<Meal> meals,  List<Recipe> recipeList, Patient patient, Nutritionist nutritionist) throws RegisterException, InfraException {
		MealPlan mp = new MealPlan(planName, new Date(), goals, meals, recipeList, patient, nutritionist);
		persistence.insert(mp);
	}

	@Override
	public void updateMealPlan(MealPlan mealPlan, String planName, String goals, List<Meal> meals, List<Recipe> recipeList) throws UpdateException, InfraException {
		MealPlan mp = new MealPlan(planName, mealPlan.getCreationDate(), goals, meals, recipeList, mealPlan.getPatient(), mealPlan.getNutritionist());
		persistence.update(mp, mealPlan.hashCode());
		
	}

	@Override
	public void deleteMealPlan(MealPlan mealPLan) throws DeleteException, InfraException {
		persistence.delete(mealPLan);
	}

	@Override
	public MealPlan retrieve(Patient patient) throws EntityNotFoundException, InfraException {
		return persistence.retrieveById(patient.hashCode());
	}
}