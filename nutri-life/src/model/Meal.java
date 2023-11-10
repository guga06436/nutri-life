package model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Meal {
	private String name;
	private String time;
	private Map<Food, Map<Float, String>> portionedFoods;
	
	public Meal() {
		
	}
	
	public Meal(String name, String time, Map<Food, Map<Float, String>> portionedFoods) {
		this.name = name;
		this.time = time;
		this.portionedFoods = portionedFoods;
	}
	
	public Meal(Meal meal) {
		this.name = meal.getName();
		this.time = meal.getTime();
		this.portionedFoods = new HashMap<>(meal.getPortionedFoods());
	}
	
	public Memento saveMemento() {
		Memento memento = new Memento();
		memento.setState(this);
		
		return memento;
	}
	
	public void restore(Memento memento) {
		Meal lastState = (Meal)memento.getState();
		
		name = lastState.getName();
		time = lastState.getTime();
		portionedFoods = lastState.getPortionedFoods();
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
