package model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Recipe {
	private String name;
	private Map<Food, Map<Float, String>> portionedIngredients;
	private List<String> sequenceSteps;
	
	public Recipe(String name, Map<Food, Map<Float, String>> portionedIngredients, List<String> sequenceSteps) {
		this.name = name;
		this.portionedIngredients = portionedIngredients;
		this.sequenceSteps = sequenceSteps;
	}
	
	// Adds a new ingredient to the recipe
	public void addNewIngredient(Food food, Map<Float, String> portionedFood) {
		portionedIngredients.put(food,  portionedFood);
	}
	
	// Adds a new step to the preparation sequence
	public void addNewStep(String step) {
		sequenceSteps.add(step);
	}
}
