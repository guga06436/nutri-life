package model;

import java.util.Map;

public class Meal {
	private String name;
	private String time;
	private Map<Food, Map<Float, String>> portionedFoods;
	
	public Meal(String name, String time, Map<Food, Map<Float, String>> portionedFoods) {
		this.name = name;
		this.time = time;
		this.portionedFoods = portionedFoods;
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
