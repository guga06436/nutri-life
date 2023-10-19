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
import model.Recipe;
import persistence.Persistence;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class RecipePersistence implements Persistence<Recipe>{
	private static FactoryFood factoryFood;
	private static FoodPersistence foodPersistence;
	private static Connection conn;
	
	public RecipePersistence() throws InfraException {
		conn = Database.getConnection();
		factoryFood = new FactoryFood();
		foodPersistence = factoryFood.getPersistence();
	}
	
	private boolean insertPortionedIngredients(Map<Food, Map<Float, String>> portionedIngredients, int recipeId) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
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
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		int recipeId = -1;
		boolean confirmationStoring = false;
		
		try {
			ps = conn.prepareStatement("INSERT INTO Recipe(recipe_name, sequence_steps) VALUES " + 
										"(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, object.getName());
			ps.setString(2, sequenceSteps(object.getSequenceSteps()));
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = ps.getGeneratedKeys();
				
				while(rs.next()) {
					recipeId = rs.getInt(1);
					
					confirmationStoring = insertPortionedIngredients(object.getPortionedIngredients(), recipeId);
				}
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to create a recipe");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to insert a recipe: null argument in method call");
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
				
				while(rs.next()) {
					recipe = new Recipe();
					
					recipe.setName(rs.getString("recipe_name"));
					recipe.setPortionedIngredients(retrievePortionedIngredients(recipeId));
					recipe.setSequenceSteps(sequenceSteps(rs.getString("sequence_steps")));
				}
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a recipe");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to insert a recipe: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return recipe;
	}

	@Override
	public boolean update(Recipe object) throws InfraException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Recipe delete(Recipe object) throws InfraException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Recipe> listAll() throws InfraException {
		Statement st = null;
		ResultSet rs = null;
		List<Recipe> allRecipes = null;
		
		try {
			st = conn.createStatement();
			
			rs = st.executeQuery("SELECT * FROM Recipe");
			
			allRecipes = new ArrayList<>();
			while(rs.next()) {
				Recipe recipe = new Recipe();		
				
				recipe.setName(rs.getString("recipe_name"));
				recipe.setPortionedIngredients(retrievePortionedIngredients(retrieveId(recipe)));
				recipe.setSequenceSteps(sequenceSteps(rs.getString("sequence_steps")));
				
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
		PreparedStatement ps = null;
		ResultSet rs = null;
		int recipeId = -1;
		
		try {
			ps = conn.prepareStatement("SELECT recipe_id FROM Recipe WHERE recipe_name = ?");
			
			ps.setString(1, object.getName());
			
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
		PreparedStatement ps = null;
		ResultSet rs = null;
		Recipe recipe = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM Recipe WHERE recipe_id = ?");
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				recipe = new Recipe();
				
				recipe.setName(rs.getString("recipe_name"));
				recipe.setPortionedIngredients(retrievePortionedIngredients(id));
				recipe.setSequenceSteps(sequenceSteps(rs.getString("sequence_steps")));
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a recipe");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to insert a recipe: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return recipe;
	}
}
