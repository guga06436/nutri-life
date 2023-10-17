package persistence.impl;

import persistence.Factory;
import persistence.db.exception.InfraException;

public class FactoryAdmin implements Factory<AdminPersistence>{

	@Override
	public AdminPersistence getPersistence() throws InfraException {
		return new AdminPersistence();
	}

}
