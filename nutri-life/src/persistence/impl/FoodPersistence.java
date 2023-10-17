package persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import model.Food;
import model.enums.FoodGroup;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class FoodPersistence {
	private static Connection conn;
	
	public FoodPersistence() throws InfraException {
		conn = Database.getConnection();
	}
	
	private Food instantiateFood(ResultSet rs) throws SQLException, InfraException {
		Food food = new Food();
		
		food.setName(rs.getString("food_name"));
		food.setFoodGroup(FoodGroup.values()[rs.getInt("food_group")]);
		food.setCalories(rs.getFloat("calories"));
		food.setProteins(rs.getFloat("proteins"));
		food.setCarbohydrates(rs.getFloat("carbohydrates"));
		food.setLipids(rs.getFloat("lipids"));
		food.setFibers(rs.getFloat("fibers"));
		
		Map<String, Map<Float, String>> vitamins = retrieveAllVitaminsFood();
		
		food.setVitamins(vitamins);
		food.setPortion(rs.getFloat("portion"));
		food.setPortionUnit(rs.getString("portion_unit"));
		
		return food;
	}
	
	private Map<String, Map<Float, String>> retrieveAllVitaminsFood() throws InfraException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, Map<Float, String>> vitamins = null;
		Map<Float, String> portion = null;
		
		try {
			vitamins = new HashMap<>();
			portion = new HashMap<>();
			
			ps = conn.prepareStatement("SELECT * FROM Vitamin WHERE food_id = ?");
			rs = ps.executeQuery();
			
			while(rs.next()) {				
				portion.put(rs.getFloat("portion"), rs.getString("portion_unit"));
				vitamins.put(rs.getString("vitamin_name"), portion);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrive all food information");
		}
		finally{
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return vitamins;
	}
	
	public Food retrieve(String name, FoodGroup foodGroup) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Food food = null;
		
		try {
			ps = conn.prepareStatement("SELECT * from Food WHERE food_name = ? AND food_group = ?");
			
			ps.setString(1, name);
			ps.setInt(2, foodGroup.ordinal());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				food = instantiateFood(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve food");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return food;
	}
	
	private boolean insertVitamins(Map<String, Map<Float, String>> vitamins, int foodId) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			ps = conn.prepareStatement("INSERT INTO Vitamin(vitamin_name, portion, portion_unit, food_id) " + 
					"VALUES (?, ?, ?, ?)");
			
				
			Iterator<String> vitaminNameIterator = vitamins.keySet().iterator();
			
			while(vitaminNameIterator.hasNext()) {
				String vitaminName = vitaminNameIterator.next();
				
				ps.setString(1, vitaminName);
				
				Map<Float, String> portion = vitamins.get(vitaminName);
				Iterator<Float> portionIterator = portion.keySet().iterator();
		
				while(portionIterator.hasNext()) {
					float portionValue = portionIterator.next();
					
					ps.setFloat(2, portionValue);
					ps.setString(3, portion.get(portionValue));
				}
			}
				
			ps.setInt(4, foodId);
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				return true;
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to enter nutritional information for the food");
		}
		
		finally {
			Database.closeStatement(ps);
		}
		
		return false;
	}
	
	public int insert(Food food) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		int foodId = -1;
		boolean confirmationFoodInsertion = false;
		
		try {	
			ps = conn.prepareStatement("INSERT INTO Food(food_name, food_group, calories, proteins, carbohydrates" + 
										", lipids, fibers, portion, portion_unit", Statement.RETURN_GENERATED_KEYS);
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = ps.getGeneratedKeys();
				
				while(rs.next()) {
					foodId = rs.getInt(1);
					
					confirmationFoodInsertion = insertVitamins(food.getVitamins(), foodId);
				}
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to insert a food into database");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		if(confirmationFoodInsertion) {
			return foodId;
		}
		
		return -1;
	}
}
