package persistence.impl;

import persistence.Factory;
import persistence.db.exception.InfraException;

public class FactoryFood implements Factory<FoodPersistence>{

	@Override
	public FoodPersistence getPersistence() throws InfraException {
		return new FoodPersistence();
	}
}
