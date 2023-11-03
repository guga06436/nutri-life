package controller.impl;

import controller.FoodManager;
import controller.exceptions.EntityNotFoundException;
import model.Food;
import model.enums.FoodGroup;
import persistence.Persistence;
import persistence.db.exception.InfraException;
import persistence.impl.FactoryFood;
import service.LogService;
import service.impl.LogAdapter;

import java.util.List;

public class FoodManagerImpl implements FoodManager {

    private static FactoryFood ff;
    private static Persistence<Food> persistence;
    static final LogService log = LogAdapter.getInstance();

    public FoodManagerImpl() throws InfraException {
        try {
            ff = new FactoryFood();
            persistence = ff.getPersistence();
        }
        catch(InfraException e) {
            log.logException(e);
            throw e;
        }
    }

    @Override
    public List<Food> retrieve(String name, FoodGroup foodGroup) throws EntityNotFoundException, InfraException {
        try {
            List<Food> matchingFoods = persistence.retrieve(name, foodGroup);
            if (matchingFoods.isEmpty()) {
                String message = "No matching foods found";
                log.logDebug(message);
                throw new EntityNotFoundException(message);
            }
            return matchingFoods;
        } catch (InfraException e) {
            log.logException(e);
            throw e;
        }
    }

}
