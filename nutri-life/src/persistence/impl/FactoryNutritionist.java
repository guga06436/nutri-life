package persistence.impl;

import persistence.Factory;
import persistence.db.exception.InfraException;

public class FactoryNutritionist implements Factory<NutritionistPersistence>{

	@Override
	public NutritionistPersistence getPersistence() throws InfraException {
		return new NutritionistPersistence();
	}

}
