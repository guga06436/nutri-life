package persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import model.Food;
import model.Meal;
import persistence.Persistence;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class MealPersistence implements Persistence<Meal>{
	private static Connection conn;
	
	public MealPersistence() throws InfraException {
		conn = Database.getConnection();
	}
	
	private Meal instantiateMeal(ResultSet rs) throws InfraException, SQLException {
		Meal meal = new Meal();
		
		meal.setName(rs.getString("meal_name"));
		meal.setTime(rs.getString("meal_time"));
		meal.setPortionedFoods(retrieveAllFoodMeal(rs.getInt("meal_id")));
		
		return meal;
	}
	
	private Map<Food, Map<Float, String>> retrieveAllFoodMeal(int mealId) throws InfraException{
		FactoryFood factoryFood = new FactoryFood();
		FoodPersistence foodPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<Food, Map<Float, String>> portionedFoods = null;
		Map<Float, String> portioning = null;
		
		try {
			portionedFoods = new HashMap<>();
			portioning = new HashMap<>();
			
			ps = conn.prepareStatement("SELECT * FROM FoodMeal WHERE meal_id = ?"); 
			
			ps.setInt(1, mealId);
			
			rs = ps.executeQuery();
			
			foodPersistence = factoryFood.getPersistence();
			while(rs.next()) {				
				portioning.put(rs.getFloat("portion"), rs.getString("portion_unit"));
				
				Food food = foodPersistence.retrieveById(rs.getInt("food_id"));
				
				if(food == null) {
					throw new InfraException("Unable to retrieve a meal");
				}
				
				portionedFoods.put(food, portioning);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrive all food information");
		}
		finally{
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return portionedFoods;
	}
	
	private boolean insertFoodMeal(Map<Food, Map<Float, String>> portionedFoods, int mealId) throws InfraException {
		FactoryFood factoryFood = new FactoryFood();
		FoodPersistence foodPersistence = null;
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			foodPersistence = factoryFood.getPersistence();
			
			ps = conn.prepareStatement("INSERT INTO FoodMeal(food_id, portion, portion_unit, meal_id) VALUES " + 
					"(?, ?, ?, ?)");
			conn.setAutoCommit(false);

			for(Food food : portionedFoods.keySet()) {
				rowsAffected = -1;
				int foodId = foodPersistence.retrieveId(food);

				if(foodId < 0) {
					foodPersistence.insert(food);
					foodId = foodPersistence.retrieveId(food);
				}

				ps.setInt(1, foodId);

				for(float portion : portionedFoods.get(food).keySet()) {
					ps.setFloat(2, portion);
					ps.setString(3, portionedFoods.get(food).get(portion));
				}

				ps.setInt(4, mealId);

				rowsAffected = ps.executeUpdate();
			}
			
			conn.commit();
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to insert meal informations");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to insert meal informations and roll back changed data");
			}
		}
		finally {
			Database.closeStatement(ps);
		}
		
		if(rowsAffected > 0) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean insert(Meal object) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean insertConfirmation = false;
		
		try {			
			ps = conn.prepareStatement("INSERT INTO Meal(meal_name, meal_time) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, object.getName());
			ps.setString(2, object.getTime());
			
			conn.setAutoCommit(false);
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				int mealId = -1;
				rs = ps.getGeneratedKeys();
			
				if(rs.next()) {
					mealId = rs.getInt(1);
					insertConfirmation = insertFoodMeal(object.getPortionedFoods(), mealId);
				}

			}
			
			conn.commit();
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to insert a meal into the database");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to insert a meal into the database and roll back changed data");
			}
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return insertConfirmation;
	}

	@Override
	public Meal retrieve(Meal object) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Meal meal = null;
		
		try {			
			ps = conn.prepareStatement("SELECT * FROM Meal WHERE meal_name = ? AND meal_time = ?");
			ps.setString(1, object.getName());
			ps.setString(2, object.getTime());
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				meal = instantiateMeal(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a meal");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return meal;
	}
	
	@Override
	public List<Meal> retrieveMatch(Meal object) throws InfraException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Meal> meals = null;
		
		try {
			meals = new ArrayList<>();
			ps = conn.prepareStatement("SELECT * FROM Meal WHERE meal_name LIKE ?");
			ps.setString(1, "%" + object.getName() + "%");
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				meals.add(instantiateMeal(rs));
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve any meal.");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve a meal: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return meals;
	}
	
	private int removeUnusedFood(Map<Food, Map<Float, String>> portionedFoods, int mealId) throws InfraException {		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		
		try {
			ps = conn.prepareStatement("SELECT food_id, food_name FROM Food WHERE food_id IN "
									+ "(SELECT food_id FROM FoodMeal WHERE mealId = ?)");
			conn.setAutoCommit(false);
			ps.setInt(1, mealId);
			
			rs = ps.executeQuery();
			
			HashSet<String> foodsName = new HashSet<>();
			
			while(rs.next()) {
				foodsName.add(rs.getString("food_name"));
			}
			
			if(!foodsName.isEmpty()) {
				HashSet<String> aux = new HashSet<>();
				
				for(Food food : portionedFoods.keySet()) {
					aux.add(food.getName());
				}
				
				foodsName.removeAll(aux);
				
				if(!foodsName.isEmpty()) {
					for(String name : foodsName) {
						rs.absolute(1);
						
						while(rs.next()) {
							
							if(rs.getString("food_name") == name) {
								rowsAffected = -1;
								
								ps = conn.prepareStatement("DELETE FROM FoodMeal WHERE food_id = ? AND meal_id = ?");
								ps.setInt(1, rs.getInt("food_id"));
								ps.setInt(2, mealId);
								
								rowsAffected = ps.executeUpdate();
							}
						}
					}
				}
				
				conn.commit();
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to remove a meal");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to remove a meal and roll back changed data");
			}
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return rowsAffected;
	}
	
	private boolean updateFoodMeal(Map<Food, Map<Float, String>> portionedFoods, int mealId) throws InfraException {
		FactoryFood factoryFood = new FactoryFood();
		FoodPersistence foodPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean updateConfirmation = false;
		
		try {
			removeUnusedFood(portionedFoods, mealId);
			
			ps = conn.prepareStatement("SELECT food_id FROM FoodMeal WHERE mealId = ?");
			conn.setAutoCommit(false);
			
			ps.setInt(1, mealId);
			
			rs = ps.executeQuery();
			
			foodPersistence = factoryFood.getPersistence();
			while(rs.next()) {
				int rowsAffected = -1;
				Food aux = foodPersistence.retrieveById(rs.getInt("food_id"));
				
				if(aux == null) {
					throw new InfraException("Unable to update a meal");
				}
				
				Map<Float, String> portioning = portionedFoods.get(aux);
				
				if(portioning != null) {
					rowsAffected = -1;
					ps = conn.prepareStatement("UPDATE FoodMeal SET portion = ? AND portion_unit = ?");
					
					for(float portion : portioning.keySet()) {
						ps.setFloat(1, portion);
						ps.setString(2, portioning.get(portion));
					}
					
					rowsAffected = ps.executeUpdate();
					
					if(rowsAffected < 0) {
						throw new InfraException("Unable to update meal information");
					}
					
					portionedFoods.remove(aux);
				}
			}
			
			if(!portionedFoods.isEmpty()) {
				insertFoodMeal(portionedFoods, mealId);
			}
			
			conn.commit();
			updateConfirmation = true;
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to update food information");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to update food information and roll back changed data");
			}
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return updateConfirmation;
	}

	@Override
	public boolean update(Meal object, int id) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		boolean updateConfirmation = false;
		
		try {
			ps = conn.prepareStatement("UPDATE Meal SET meal_name = ?, meal_time = ?");
			conn.setAutoCommit(false);
			ps.setString(1, object.getName());
			ps.setString(2, object.getTime());
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				updateConfirmation = updateFoodMeal(new HashMap<>(object.getPortionedFoods()), id);
			}
			
			conn.commit();
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to udapte a meal");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to udapte a meal and roll back changed data");
			}
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return updateConfirmation;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean delete(Meal object) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {			
			int mealId = retrieveId(object);
			
			ps = conn.prepareStatement("DELETE FROM MealMealPlan WHERE meal_id = ?");
			ps.setInt(1, mealId);
			
			conn.setAutoCommit(false);
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rowsAffected = -1;
				
				ps = conn.prepareStatement("DELETE FROM FoodMeal WHERE meal_id = ?");
				ps.setInt(1, mealId);
				
				ps.executeUpdate();
				
				ps = conn.prepareStatement("DELETE FROM Meal WHERE meal_id = ? AND meal_name = ?");
				ps.setInt(1, mealId);
				ps.setString(2, object.getName());
				
				rowsAffected = ps.executeUpdate();
				
				conn.commit();
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to delete a meal");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to delete a meal and roll back changed data");
			}
		}
		finally {
			Database.closeStatement(ps);
		}
		
		if(rowsAffected > 0) {
			return true;
		}
		
		return false;
	}

	@Override
	public List<Meal> listAll() throws InfraException {
		Statement st = null;
		ResultSet rs = null;
		List<Meal> allMeals;
		
		try {
			st = conn.createStatement();
			
			rs = st.executeQuery("SELECT * FROM Meal");
			
			allMeals = new ArrayList<>();
			while(rs.next()) {
				Meal meal = instantiateMeal(rs);
				
				allMeals.add(meal);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve all meals");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(st);
		}
		
		return allMeals;
	}

	@Override
	public int retrieveId(Meal object) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int mealId = -1;
		
		try {			
			ps = conn.prepareStatement("SELECT meal_id FROM Meal WHERE meal_name = ? AND meal_time = ?");
			ps.setString(1, object.getName());
			ps.setString(2, object.getTime());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				mealId = rs.getInt(1);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve meal ID");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return mealId;
	}

	@Override
	public Meal retrieveById(int id) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Meal meal = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM Meal WHERE meal_id = ?");
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				meal = instantiateMeal(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a meal");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return meal;
	}
}
