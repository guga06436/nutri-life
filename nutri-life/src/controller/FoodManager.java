package controller;

import controller.exceptions.EntityNotFoundException;
import model.Food;
import model.enums.FoodGroup;
import persistence.db.exception.InfraException;

import java.util.List;

public interface FoodManager {
    List<Food> retrieve(String name, FoodGroup foodGroup) throws EntityNotFoundException, InfraException;
}
