package persistence.impl;

import persistence.Factory;
import persistence.db.exception.InfraException;

public class FactoryMealPlan implements Factory<MealPlanPersistence>{
	private static MealPlanPersistence mpp = null;
	
	public FactoryMealPlan() throws InfraException {
		mpp = new MealPlanPersistence();
	}
	
	public MealPlanPersistence getPersistence() throws InfraException {
		return mpp;
	}
}
