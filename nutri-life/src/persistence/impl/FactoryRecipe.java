package persistence.impl;

import persistence.Factory;
import persistence.db.exception.InfraException;

public class FactoryRecipe implements Factory<RecipePersistence>{

	@Override
	public RecipePersistence getPersistence() throws InfraException {
		return new RecipePersistence();
	}

}
