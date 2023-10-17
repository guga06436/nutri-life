package persistence.impl;

import persistence.Factory;
import persistence.db.exception.InfraException;

public class FactoryMealPlan implements Factory<MealPlanPersistence>{

	@Override
	public MealPlanPersistence getPersistence() throws InfraException {
		return new MealPlanPersistence();
	}

}
