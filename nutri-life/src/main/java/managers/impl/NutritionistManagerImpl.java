package managers.impl;

import entities.Nutritionist;
import managers.NutritionistManager;
import persistence.NutritionistPersistence;

public class NutritionistManagerImpl implements NutritionistManager{

    public NutritionistManagerImpl() {

    }

    @Override
    public boolean add(Nutritionist n) {
        return NutritionistPersistence.nutritionistPersistence.add(n);
    }

    @Override
    public void listAll() {
        for(Nutritionist n: NutritionistPersistence.nutritionistPersistence) {
            System.out.println(n);
        }
    }
}