package persistence.impl;

import persistence.Factory;
import persistence.db.exception.InfraException;

public class FactoryAdmin implements Factory<AdminPersistence>{
	private static AdminPersistence ap = null;
	
	public FactoryAdmin() throws InfraException {
		ap = new AdminPersistence();
	}
	
	public AdminPersistence getPersistence() throws InfraException {
		return ap;
	}
}
