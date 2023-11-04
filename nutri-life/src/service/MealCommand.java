package service;

import model.Meal;

public interface MealCommand<T>{
	public T execute(Meal meal) throws Exception;
}
