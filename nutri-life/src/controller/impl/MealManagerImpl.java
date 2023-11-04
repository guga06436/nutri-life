package controller.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.MealManager;
import controller.exceptions.DeleteException;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.exceptions.UpdateException;
import model.Meal;
import model.MealPlan;
import persistence.Persistence;
import persistence.db.exception.InfraException;
import persistence.impl.FactoryMeal;
import service.LogService;
import service.impl.LogAdapter;

public class MealManagerImpl implements MealManager {
    private static FactoryMeal mf;
    private static Persistence<Meal> persistence;
    private static final LogService log = LogAdapter.getInstance();

    public MealManagerImpl() throws InfraException {
        try {
            mf = new FactoryMeal();
            persistence = mf.getPersistence();
        } catch (InfraException e) {
            log.logException(e);
            throw e;
        }
    }

    @Override
    public boolean insert(Meal meal) throws InfraException, RegisterException {
        try {
            validateTime(meal.getTime());
            return persistence.insert(meal);
            
        } catch (RegisterException e) {
            log.logException(e);
            throw e;
        } catch (InfraException e) {
            log.logException(e);
            throw e;
        }
    }

    private boolean validateTime(String time) throws RegisterException {
    	Pattern pattern = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");
    	Matcher matcher = pattern.matcher(time);
    	
        if (matcher.matches()) {
            return true;
        }
        
        return false;
    }

    @Override
    public void deleteMeal(Meal meal) throws DeleteException {
        // TODO Auto-generated method stub
    }

    private void validateMealPlan(MealPlan mealPlan) throws IllegalArgumentException {
        if (mealPlan == null) {
            throw new IllegalArgumentException("Meal plan cannot be null");
        }

        if (isEmptyString(mealPlan.getPlanName())) {
            throw new IllegalArgumentException("Meal plan name cannot be empty");
        }
        // You can add more attribute checks here as needed.
    }

    private boolean isEmptyString(String str) {
        return str == null || str.trim().isEmpty();
    }

	@Override
	public boolean updateMeal(Meal meal) throws UpdateException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Meal> retrieve(Meal meal) throws EntityNotFoundException, InfraException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meal> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meal retrieveById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int retrieveId(Meal meal) {
		// TODO Auto-generated method stub
		return 0;
	}
}
