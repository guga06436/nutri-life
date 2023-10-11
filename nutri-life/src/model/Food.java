package model;

import java.util.Map;

import lombok.Data;
import model.enums.FoodGroup;

@Data
public class Food {
	private String name;
	private FoodGroup foodGroup;
	private float calories;
	private float proteins;
	private float carbohydrates;
	private float lipids;
	private float fibers;
	private Map<String, Float> vitamins;
	private float portion;
	private String portionUnit;
	
	public Food(String name, FoodGroup foodGroup, float calories, float proteins, float carbohydrates, float lipids, float fibers, Map<String, Float> vitamins, float portion, String portionUnit) {
		this.name = name;
		this.foodGroup = foodGroup;
		this.calories = calories;
		this.proteins = proteins;
		this.carbohydrates = carbohydrates;
		this.lipids = lipids;
		this.fibers = fibers;
		this.vitamins = vitamins;
		this.portion = portion;
		this.portionUnit = portionUnit;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Food other = (Food) obj;
		if (Float.floatToIntBits(calories) != Float.floatToIntBits(other.calories))
			return false;
		if (foodGroup != other.foodGroup)
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
		result = prime * result + Float.floatToIntBits(calories);
		result = prime * result + ((foodGroup == null) ? 0 : foodGroup.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return this.name + "(calories: " + this.calories + ")";
	}
}