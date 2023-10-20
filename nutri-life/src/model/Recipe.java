package model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Recipe {
	private String name;
	private Map<Food, Map<Float, String>> portionedIngredients;
	private List<String> sequenceSteps;
	
	private MealPlan mealPlan;
	
	public Recipe() {
		
	}
	
	public Recipe(String name, Map<Food, Map<Float, String>> portionedIngredients, List<String> sequenceSteps, MealPlan mealPlan) {
		this.name = name;
		this.portionedIngredients = portionedIngredients;
		this.sequenceSteps = sequenceSteps;
		this.mealPlan = mealPlan;
	}
	
	// Adds a new ingredient to the recipe
	public void addNewIngredient(Food food, Map<Float, String> portionedFood) {
		portionedIngredients.put(food,  portionedFood);
	}
	
	// Adds a new step to the preparation sequence
	public void addNewStep(String step) {
		sequenceSteps.add(step);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipe other = (Recipe) obj;
		if (mealPlan == null) {
			if (other.mealPlan != null)
				return false;
		} else if (!mealPlan.equals(other.mealPlan))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mealPlan == null) ? 0 : mealPlan.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return name + " (Patient: " + mealPlan.getPatient() + ")";
	}
}
