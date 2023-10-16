package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
		
		return vitamins;
	}
	
	private int retrieveFoodId(String foodName) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = -1;
		
		try {
			ps = conn.prepareStatement("SELECT food_id FROM Food WHERE food_name = ?");
			
			ps.setString(1, foodName);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				id = rs.getInt(1);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a food ID");
		}
		
		return id;
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
		
		return food;
	}
}
