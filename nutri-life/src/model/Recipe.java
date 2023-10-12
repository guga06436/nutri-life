package model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Recipe {
	private String name;
	private Map<String, FoodPortion> portionedIngredients;
	private List<String> sequenceSteps;
	
	public Recipe(String name, Map<String, FoodPortion> portionedIngredients, List<String> sequenceSteps) {
		this.name = name;
		this.portionedIngredients = portionedIngredients;
		this.sequenceSteps = sequenceSteps;
	}
	
	// Adds a new ingredient to the recipe
	public void addNewIngredient(String name, FoodPortion foodPortion) {
		portionedIngredients.put(name,  foodPortion);
	}
	
	// Adds a new step to the preparation sequence
	public void addNewStep(String step) {
		sequenceSteps.add(step);
	}
}
