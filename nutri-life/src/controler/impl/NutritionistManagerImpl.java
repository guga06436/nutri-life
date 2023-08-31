<<<<<<< HEAD:nutri-life/src/main/java/managers/impl/NutritionistManagerImpl.java
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
=======
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
		return np.add(n);
	}
>>>>>>> 077c0cb370a2fa69a196862a944be826d981f9f1:nutri-life/src/controler/impl/NutritionistManagerImpl.java
}