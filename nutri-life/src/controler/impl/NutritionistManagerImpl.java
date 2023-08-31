package controler.impl;

import controler.NutritionistManager;
import model.Nutritionist;
import persistence.NutritionistPersistence;

public class NutritionistManagerImpl implements NutritionistManager{
	private NutritionistPersistence np;
	
	public NutritionistManagerImpl() {
		np = new NutritionistPersistence();
	}
	
	@Override
	public boolean add(Nutritionist n) {
		return n.add(n);
	}

    @Override
    public void listAll() {
        for(Nutritionist n: NutritionistPersistence.nutritionistPersistence) {
            System.out.println(n);
        }
    }
}