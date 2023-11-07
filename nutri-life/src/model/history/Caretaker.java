package model.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Meal;
import model.Memento;

public class Caretaker {
	List<Meal> originators;
	Map<Meal, Memento> states;
	
	public Caretaker() {
		originators = new ArrayList<>();
		states = new HashMap<>();
	}
	
	public void setOriginator(Meal meal) {
		originators.add(meal);
	}
	
	public void removeOriginator(Meal meal) {
		if(originators.contains(meal)) {
			originators.remove(meal);
			
			if(states.containsKey(meal)) {
				states.remove(meal);
			}
		}
	}
	
	public void saveState(Meal meal) {
		for(Meal mealOriginator: originators) {
			if(mealOriginator.getName() == meal.getName()) {
				states.put(meal, meal.saveMemento());
			}
		}
	}
	
	public void undo(Meal meal) {
		if(states.containsKey(meal)) {
			meal.restore(states.get(meal));
		}
	}
}
