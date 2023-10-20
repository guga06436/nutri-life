package model;

import java.util.Map;

import lombok.Data;

@Data
public class Meal {
	private String name;
	private String time;
	private Map<Food, Map<Float, String>> portionedFoods;
	
	private MealPlan mealPlan;
	
	public Meal(String name, String time, Map<Food, Map<Float, String>> portionedFoods, MealPlan mealPlan) {
		this.name = name;
		this.time = time;
		this.portionedFoods = portionedFoods;
		this.mealPlan = mealPlan;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Meal other = (Meal) obj;
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
		StringBuilder sb = new StringBuilder();
		
		sb.append(name + ": \n\n");
		
		for(Food food : portionedFoods.keySet()) {
			sb.append("(" + time + ") " + food + " - ");
			
			for(float portion : portionedFoods.get(food).keySet()) {
				sb.append(portion);
				sb.append(portionedFoods.get(food).get(portion));
			}
		}
		
		return sb.toString();
	}
}
