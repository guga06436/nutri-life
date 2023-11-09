package persistence;

import model.Nutritionist;
import model.Patient;
import persistence.db.exception.InfraException;

public interface PatientPersistenceExs extends Persistence<Patient>{
	Nutritionist retrivePatientNutritionist(Patient patient) throws InfraException ;
}
