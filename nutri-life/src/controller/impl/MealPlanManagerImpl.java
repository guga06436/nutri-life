package controller.impl;

import java.util.Date;
import java.util.List;

import controller.MealPlanManager;
import controller.exceptions.DeleteException;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.exceptions.UpdateException;
import lombok.Data;
import model.Meal;
import model.MealPlan;
import model.Nutritionist;
import model.Patient;
import persistence.Persistence;
import persistence.db.exception.InfraException;
import persistence.impl.FactoryMealPlan;
import service.LogService;
import service.impl.LogAdapter;

@Data
public class MealPlanManagerImpl implements MealPlanManager{
	private static FactoryMealPlan fmp;
	private static Persistence<MealPlan> persistence;
	private static final LogService log = LogAdapter.getInstance();
	
	public MealPlanManagerImpl() throws InfraException {
		try {
			fmp = new FactoryMealPlan();
			persistence = fmp.getPersistence();
		}
		catch(InfraException e) {
			log.logException(e);
			throw e;
		}
	}
	
	@Override
	public void createMealPlan(String planName, String goals,List<Meal> meals, Patient patient, Nutritionist nutritionist) throws RegisterException, InfraException {
		try {
			MealPlan mp = new MealPlan(planName, new Date(), goals, meals, patient, nutritionist);
			persistence.insert(mp);
		} catch (InfraException e) {
			log.logException(e);
			throw e;
		}

	}

	@Override
	public void updateMealPlan(MealPlan mealPlan, String planName, String goals, List<Meal> meals) throws UpdateException, InfraException {
		try {
			if (planName == null) {
				String message = "Plan Name must not be null";

				log.logDebug(message);
				throw new UpdateException(message);
			}
			MealPlan mp = new MealPlan(planName, mealPlan.getCreationDate(), goals, meals, mealPlan.getPatient(), mealPlan.getNutritionist());
			persistence.update(mp, persistence.retrieveId(mp));
		} catch (InfraException e) {
			log.logException(e);
			throw e;
		}
	}

	@Override
	public void deleteMealPlan(MealPlan mealPLan) throws DeleteException, InfraException {
		try {
			if (mealPLan == null) {
				String message = "mealPlan not found";

				log.logDebug(message + "[Trying to delete null Meal Plan]");
				throw new DeleteException(message);
			}
			persistence.delete(mealPLan);
		} catch (InfraException e) {
			log.logException(e);
			throw e;
		}
	}

	@Override
	public MealPlan retrieve(Patient patient) throws EntityNotFoundException, InfraException {
		try {
			MealPlan mealPlan = persistence.retrieve(patient.getMealPlan());
			if (mealPlan == null) {
				String message = "MealPlan not found";
				log.logDebug(message);
				throw new EntityNotFoundException(message);
			}
			return mealPlan;
		} catch (InfraException e) {
			log.logException(e);
			throw e;
		}
	}
}