package persistence.impl;

import persistence.Factory;
import persistence.db.exception.InfraException;

public class FactoryMeal implements Factory<MealPersistence>{

	@Override
	public MealPersistence getPersistence() throws InfraException {
		return new MealPersistence();
	}

}
