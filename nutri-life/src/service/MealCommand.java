package service;

import java.util.List;

import model.Meal;

public interface MealCommand<T>{
	public T execute(List<Meal> meals) throws Exception;
}
