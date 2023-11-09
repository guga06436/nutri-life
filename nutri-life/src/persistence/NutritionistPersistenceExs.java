package persistence;

import java.util.List;

import model.Nutritionist;
import model.Patient;
import persistence.db.exception.InfraException;

public interface NutritionistPersistenceExs extends Persistence<Nutritionist>{
	List<Patient> listAllNutritionistPatients(Nutritionist nutritionist)  throws InfraException;
	boolean addNewPatient(Nutritionist nutritionist, Patient patient) throws InfraException;
}
