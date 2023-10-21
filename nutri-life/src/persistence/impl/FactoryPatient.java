package persistence.impl;

import persistence.Factory;
import persistence.db.exception.InfraException;

public class FactoryPatient implements Factory<PatientPersistence>{

	@Override
	public PatientPersistence getPersistence() throws InfraException {
		return new PatientPersistence();
	}

}
