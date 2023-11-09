package persistence;

import model.MealPlan;
import model.Patient;
import persistence.db.exception.InfraException;

public interface MealPlanPersistenceExs extends Persistence<MealPlan>{
	MealPlan retrieveByPatient(Patient patient)  throws InfraException ;
}
