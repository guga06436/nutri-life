package persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Food;
import model.MealPlan;
import model.Recipe;
import persistence.Persistence;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class RecipePersistence implements Persistence<Recipe>{
	private static Connection conn;
	
	public RecipePersistence() throws InfraException {
		conn = Database.getConnection();
	}
	
	private boolean insertPortionedIngredients(Map<Food, Map<Float, String>> portionedIngredients, int recipeId) throws InfraException {
		FactoryFood factoryFood = new FactoryFood();
		FoodPersistence foodPersistence = null;
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			foodPersistence = factoryFood.getPersistence();
			
			for(Food food : portionedIngredients.keySet()) {
				Food aux = foodPersistence.retrieve(food);
				
				if(aux == null) {
					foodPersistence.insert(food);
				}
				
				int foodId = foodPersistence.retrieveId(food);
				
				for(float portion : portionedIngredients.get(food).keySet()) {
					ps = conn.prepareStatement("INSERT INTO FoodRecipe(food_id, portion, portion_unit, recipe_id) " +
												"VALUES (?, ?, ?, ?)");
					
					ps.setInt(1, foodId);
					ps.setFloat(2, portion);
					ps.setString(3, portionedIngredients.get(food).get(portion));
					ps.setInt(4, recipeId);
					
					rowsAffected = ps.executeUpdate();
				}
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to store recipe information");
		}
		finally {
			Database.closeStatement(ps);
		}
		
		if(rowsAffected > 0) {
			return true;
		}
		
		return false;
	}
	
	private String sequenceSteps(List<String> steps) {
		StringBuilder sb = new StringBuilder();
		
		for (String s: steps) {
			sb.append(s).append("?");
		}
		
		if(!steps.isEmpty()) {
			sb.setLength(sb.length() - 1 );
		}
		
		return sb.toString();
	}

	@Override
	public boolean insert(Recipe object) throws InfraException {
		FactoryMealPlan factoryMp = new FactoryMealPlan();
		MealPlanPersistence mpPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		int recipeId = -1;
		boolean confirmationStoring = false;
		
		try {
			mpPersistence = factoryMp.getPersistence();
			
			int mealPlanId = mpPersistence.retrieveId(object.getMealPlan());
			
			if(mealPlanId < 0) {
				mpPersistence.insert(object.getMealPlan());
				mealPlanId = mpPersistence.retrieveId(object.getMealPlan());
			}
			
			ps = conn.prepareStatement("INSERT INTO Recipe(recipe_name, sequence_steps, mealplan_id) VALUES " + 
										"(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			conn.setAutoCommit(false);
			
			ps.setString(1, object.getName());
			ps.setString(2, sequenceSteps(object.getSequenceSteps()));
			ps.setInt(3, mealPlanId);
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = ps.getGeneratedKeys();
				
				while(rs.next()) {
					recipeId = rs.getInt(1);
					
					confirmationStoring = insertPortionedIngredients(object.getPortionedIngredients(), recipeId);
				}
				
				conn.commit();
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to create a recipe");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to create a recipe and roll back changed data");
			}
		}
		catch(NullPointerException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to insert a recipe: null argument in method call");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to insert a recipe and roll back changed data");
			}
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return confirmationStoring;
	}
	
	private Map<Food, Map<Float, String>> retrievePortionedIngredients(int recipeId) throws InfraException, NullPointerException{
		FactoryFood factory = new FactoryFood();
		FoodPersistence foodPersistence = null;
		Map<Food, Map<Float, String>> portionedIngredients = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {			
			portionedIngredients = new HashMap<>();
			
			ps = conn.prepareStatement("SELECT * FROM FoodRecipe WHERE recipe_id = ?");
			
			ps.setInt(1, recipeId);
			
			rs = ps.executeQuery();
			
			foodPersistence = factory.getPersistence();
			Map<Float, String> portioning = null;
			while(rs.next()) {
				portioning = new HashMap<>();
				int foodId = rs.getInt("food_id");
				
				Food food = foodPersistence.retrieveById(foodId);

				if(food == null) {
					throw new NullPointerException();
				}
				
				portioning.put(rs.getFloat("portion"), rs.getString("portion_unit"));
				
				portionedIngredients.put(food, portioning);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve recipe information");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return portionedIngredients;
	}
	
	private List<String> sequenceSteps(String steps){
		return Arrays.asList(steps.split("\\?"));
	}

	@Override
	public Recipe retrieve(Recipe object) throws InfraException {
		FactoryMealPlan factoryMp = new FactoryMealPlan();
		MealPlanPersistence mpPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int recipeId = -1;
		Recipe recipe = null;
		
		try {
			recipeId = retrieveId(object);
			
			if(recipeId != -1) {
				ps = conn.prepareStatement("SELECT * FROM Recipe WHERE recipe_id = ?");
				
				ps.setInt(1, recipeId);
				
				rs = ps.executeQuery();
				
				mpPersistence = factoryMp.getPersistence();
				while(rs.next()) {
					recipe = new Recipe();
					
					recipe.setName(rs.getString("recipe_name"));
					recipe.setPortionedIngredients(retrievePortionedIngredients(recipeId));
					recipe.setSequenceSteps(sequenceSteps(rs.getString("sequence_steps")));
					
					MealPlan mealPlan = mpPersistence.retrieveById(rs.getInt("mealplan_id"));
					
					if(mealPlan == null) {
						throw new InfraException("Unable to retrieve a recipe");
					}
					
					recipe.setMealPlan(mealPlan);
				}
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a recipe");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve a recipe: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return recipe;
	}
	
	private boolean updatePortionedIngredients(Map<Food, Map<Float, String>> portionedIngredients, int recipeId) throws InfraException{
		FactoryFood factory = new FactoryFood();
		FoodPersistence foodPersistence = null;
		PreparedStatement ps = null;
		boolean updateConfirmation = false;
		int rowsAffected = -1;
		
		try {						
			foodPersistence = factory.getPersistence();
			
			for(Food food : portionedIngredients.keySet()) {
				int foodId = foodPersistence.retrieveId(food);
				
				if(foodId < 0) {
					throw new InfraException("Unable to retrieve recipe information");
				}
				
				if(foodPersistence.update(food, foodId)) {
					ps = conn.prepareStatement("UPDATE FoodRecipe SET portion = ?, portion_unit = ? WHERE recipe_id = ?");
					
					for(float portion : portionedIngredients.get(food).keySet()) {
						ps.setFloat(1, portion);
						ps.setString(2, portionedIngredients.get(food).get(portion));
						ps.setInt(3, recipeId);
					}
					
					rowsAffected = ps.executeUpdate();
					
					if(rowsAffected < 0) {
						throw new InfraException("Unable to update recipe information");
					}
				}
			}
			
			updateConfirmation = true;
		}
		catch(SQLException e) {
			throw new InfraException("Unable to update recipe information");
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return updateConfirmation;
	}

	@Override
	public boolean update(Recipe object, int id) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		boolean updateConfirmation = true;
		
		try {
			Recipe recipe = retrieveById(id);
			
			if(recipe == null) {
				updateConfirmation = insert(object);
			}
			else {
				recipe.setName(object.getName());
				recipe.setPortionedIngredients(object.getPortionedIngredients());
				recipe.setSequenceSteps(object.getSequenceSteps());
				
				ps = conn.prepareStatement("UPDATE Recipe SET recipe_name = ?, sequence_steps = ? WHERE recipe_id = ?");
				conn.setAutoCommit(false);
				
				ps.setString(1, object.getName());
				ps.setString(2, sequenceSteps(object.getSequenceSteps()));
				ps.setInt(3, id);
				
				rowsAffected = ps.executeUpdate();
				
				if(rowsAffected > 0) {
					updateConfirmation = updatePortionedIngredients(object.getPortionedIngredients(), id);
					conn.commit();
				}
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to update a recipe");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to update a recipe and roll back changed data");
			}
		}
		catch(NullPointerException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to update a recipe: null argument in method call");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to update a recipe: null argument in method call and roll back changed data");
			}
		}
		finally {
			Database.closeStatement(ps);
		}
		
		if(updateConfirmation) {
			return true;
		}
		
		return false;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean delete(Recipe object) throws InfraException {
		FactoryMealPlan factoryMp = new FactoryMealPlan();
		MealPlanPersistence mpPersistence = null;
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			mpPersistence = factoryMp.getPersistence();
			
			int mealPlanId = mpPersistence.retrieveId(object.getMealPlan());
			
			if(mealPlanId < 0) {
				throw new InfraException("Unable to delete a recipe from database");
			}
			
			ps = conn.prepareStatement("DELETE FROM Recipe WHERE recipe_id = ? AND mealplan_id = ?");
			conn.setAutoCommit(false);
			
			ps.setInt(1, retrieveId(object));
			ps.setInt(2, mealPlanId);
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rowsAffected = -1;
				ps = conn.prepareStatement("DELETE FROM FoodRecipe WHERE recipe_id = ?");
				
				ps.setInt(1, retrieveId(object));
				
				rowsAffected = ps.executeUpdate();
				conn.commit();
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Cannot delete a recipe");
			}
			catch(SQLException r) {
				throw new InfraException("Cannot delete a recipe and roll back changed data");
			}
		}
		catch(NullPointerException e) {
			try {
				conn.rollback();
				throw new InfraException("Cannot delete a recipe: null argument in method call");
			}
			catch(SQLException r) {
				throw new InfraException("Cannot delete a recipe and roll back changed data: null argument in method call");
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
	public List<Recipe> listAll() throws InfraException {
		FactoryMealPlan factoryMp = new FactoryMealPlan();
		MealPlanPersistence mpPersistence = null;
		Statement st = null;
		ResultSet rs = null;
		List<Recipe> allRecipes = null;
		
		try {
			st = conn.createStatement();
			
			rs = st.executeQuery("SELECT * FROM Recipe");
			
			allRecipes = new ArrayList<>();
			mpPersistence = factoryMp.getPersistence();
			while(rs.next()) {
				Recipe recipe = new Recipe();		
				
				recipe.setName(rs.getString("recipe_name"));
				recipe.setPortionedIngredients(retrievePortionedIngredients(retrieveId(recipe)));
				recipe.setSequenceSteps(sequenceSteps(rs.getString("sequence_steps")));
				
				MealPlan mealPlan = mpPersistence.retrieveById(rs.getInt("mealplan_id"));
				
				if(mealPlan == null) {
					throw new InfraException("Unable to list all recipes");
				}
				
				recipe.setMealPlan(mealPlan);
				
				allRecipes.add(recipe);
			}			
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve all recipes");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(st);
		}
		
		return allRecipes;
	}

	@Override
	public int retrieveId(Recipe object) throws InfraException {
		FactoryMealPlan factoryMp = new FactoryMealPlan();
		MealPlanPersistence mpPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int recipeId = -1;
		
		try {
			mpPersistence = factoryMp.getPersistence();
			
			int mealPlanId = mpPersistence.retrieveId(object.getMealPlan());
			
			if(mealPlanId < 0) {
				throw new InfraException("Unable to retrieve the recipe");
			}
			
			ps = conn.prepareStatement("SELECT recipe_id FROM Recipe WHERE recipe_name = ? AND mealplan_id");
			
			ps.setString(1, object.getName());
			ps.setInt(2, mealPlanId);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				recipeId = rs.getInt(1);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve recipe ID");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve recipe ID: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return recipeId;
	}

	@Override
	public Recipe retrieveById(int id) throws InfraException {
		FactoryMealPlan factoryMp = new FactoryMealPlan();
		MealPlanPersistence mpPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Recipe recipe = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM Recipe WHERE recipe_id = ?");
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			mpPersistence = factoryMp.getPersistence();
			while(rs.next()) {
				recipe = new Recipe();
				
				recipe.setName(rs.getString("recipe_name"));
				recipe.setPortionedIngredients(retrievePortionedIngredients(id));
				recipe.setSequenceSteps(sequenceSteps(rs.getString("sequence_steps")));
				
				MealPlan mealPlan = mpPersistence.retrieveById(rs.getInt("mealplan_id"));
				
				if(mealPlan == null) {
					throw new InfraException("Unable to retrieve the recipe");
				}
				
				recipe.setMealPlan(mealPlan);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a recipe");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve a recipe: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return recipe;
	}
}
