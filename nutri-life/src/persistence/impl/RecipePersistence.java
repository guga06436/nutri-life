package persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
			Database.closeStatement(ps);
		}
		
		return confirmationStoring;
	}

	@Override
	public Recipe retrieve(Recipe object) throws InfraException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
}
