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
import model.enums.FoodGroup;
import persistence.Persistence;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class FoodPersistence implements Persistence<Food>{
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
		
		Map<String, Map<Float, String>> vitamins = retrieveAllVitaminsFood(rs.getInt("food_id"));
		
		food.setVitamins(vitamins);
		food.setPortion(rs.getFloat("portion"));
		food.setPortionUnit(rs.getString("portion_unit"));
		
		return food;
	}
	
	private Map<String, Map<Float, String>> retrieveAllVitaminsFood(int foodId) throws InfraException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, Map<Float, String>> vitamins = null;
		Map<Float, String> portion = null;
		
		try {
			vitamins = new HashMap<>();
			portion = new HashMap<>();
			
			ps = conn.prepareStatement("SELECT * FROM Vitamin WHERE food_id = ?"); 
			
			ps.setInt(1, foodId);
			
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
	
	@Override
	public Food retrieve(Food food) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Food foodAux = null;
		
		try {
			ps = conn.prepareStatement("SELECT * from Food WHERE food_name = ? AND food_group = ?");
			
			ps.setString(1, food.getName());
			ps.setInt(2, food.getFoodGroup().ordinal());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				foodAux = instantiateFood(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve food");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve a food: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return foodAux;
	}
	
	private boolean insertVitamins(Map<String, Map<Float, String>> vitamins, int foodId) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			
			for(String vitaminName : vitamins.keySet()) {
				rowsAffected = -1; 
				
				ps = conn.prepareStatement("INSERT INTO Vitamin(vitamin_name, portion, portion_unit, food_id) " + 
						"VALUES (?, ?, ?, ?)");
				
				ps.setString(1, vitaminName);
				
				for(float portion : vitamins.get(vitaminName).keySet()) {
					ps.setFloat(2, portion);
					ps.setString(3, vitamins.get(vitaminName).get(portion));
				}
				
				ps.setInt(4, foodId);
				
				rowsAffected = ps.executeUpdate();
				
				if(rowsAffected < 0) {
					throw new InfraException("Unable to insert a food into database");
				}
			}
			
		}
		catch(SQLException e) {
			throw new InfraException("Unable to insert a food into database");
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
	public boolean insert(Food food) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		int foodId = -1;
		boolean confirmationFoodInsertion = false;
		
		try {	
			ps = conn.prepareStatement("INSERT INTO Food(food_name, food_group, calories, proteins, carbohydrates" + 
										", lipids, fibers, portion, portion_unit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
										, Statement.RETURN_GENERATED_KEYS);
			conn.setAutoCommit(false);
			
			ps.setString(1, food.getName());
			ps.setInt(2, food.getFoodGroup().ordinal());
			ps.setFloat(3, food.getCalories());
			ps.setFloat(4, food.getProteins());
			ps.setFloat(5, food.getCarbohydrates());
			ps.setFloat(6, food.getLipids());
			ps.setFloat(7, food.getFibers());
			ps.setFloat(8, food.getPortion());
			ps.setString(9, food.getPortionUnit());
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = ps.getGeneratedKeys();
				
				while(rs.next()) {
					foodId = rs.getInt(1);
					
					confirmationFoodInsertion = insertVitamins(food.getVitamins(), foodId);
				}
			}
			
			conn.commit();
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to insert a food into database");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to insert a food into database and roll back changed data");
			}
		}
		catch(NullPointerException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to insert a food: null argument in method call");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to insert a food and roll back changed data: null argument in method call");
			}
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return confirmationFoodInsertion;
	}

	@Override
	public List<Food> listAll() throws InfraException {
		Statement st = null;
		ResultSet rs = null;
		List<Food> allFoods = null;
		
		try {
			st = conn.createStatement();
			
			rs = st.executeQuery("SELECT * FROM Food");
			
			allFoods = new ArrayList<>();
			while(rs.next()) {
				allFoods.add(instantiateFood(rs));
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve all foods from database");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(st);
		}
		
		return allFoods;
	}

	@Override
	public int retrieveId(Food object) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int foodId = -1;
		
		try {
			ps = conn.prepareStatement("SELECT food_id FROM Food WHERE food_name = ? AND food_group = ? AND calories = ?");
			
			ps.setString(1, object.getName());
			ps.setInt(2, object.getFoodGroup().ordinal());
			ps.setFloat(3, object.getCalories());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				foodId = rs.getInt(1);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve food");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve a food: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return foodId;
		
	}

	@Override
	public Food retrieveById(int id) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Food food = null;
		
		try {
			ps = conn.prepareStatement("SELECT * from Food WHERE food_id = ?");
			
			ps.setInt(1, id);
			
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
	
	private int removeUnusedVitamin(Map<String, Map<Float, String>> vitamins, int foodId) throws InfraException {		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		
		try {
			ps = conn.prepareStatement("SELECT vitamin_name FROM Vitamin WHERE food_id = ?");
			conn.setAutoCommit(false);
			ps.setInt(1, foodId);
			
			rs = ps.executeQuery();
			
			HashSet<String> vitaminsName = new HashSet<>();
			
			while(rs.next()) {
				vitaminsName.add(rs.getString(1));
			}
			
			if(!vitaminsName.isEmpty()) {
				vitaminsName.removeAll(vitamins.keySet());
				
				if(!vitaminsName.isEmpty()) {
					for(String name : vitaminsName) {
						rowsAffected = -1;
						
						ps = conn.prepareStatement("DELETE FROM Vitamin WHERE vitamin_name = ? AND food_id = ?");
						ps.setString(1, name);
						ps.setInt(2, foodId);
						
						rowsAffected = ps.executeUpdate();
					}
				}
			}
			
			conn.commit();
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
		
		return rowsAffected;
	}
	
	private boolean updateVitamins(Map<String, Map<Float, String>> vitamins, int foodId) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean updateConfirmation = false;
		
		try {
			removeUnusedVitamin(vitamins, foodId);
			
			ps = conn.prepareStatement("SELECT vitamin_id, vitamin_name FROM Vitamin WHERE food_id = ?");
			conn.setAutoCommit(false);
			ps.setInt(1, foodId);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				int rowsAffected = -1;
				String vitaminName = rs.getString("vitamin_name");
				Map<Float, String> portioning = vitamins.get(vitaminName);
				
				if(portioning != null) {
					rowsAffected = -1;
					ps = conn.prepareStatement("UPDATE Vitamin SET portion = ? AND portion_unit = ?");
					
					for(float portion : portioning.keySet()) {
						ps.setFloat(1, portion);
						ps.setString(2, portioning.get(portion));
					}
					
					rowsAffected = ps.executeUpdate();
					
					if(rowsAffected < 0) {
						throw new InfraException("Unable to update food information");
					}
					
					vitamins.remove(vitaminName);
				}
			}
			
			if(!vitamins.isEmpty()) {
				updateConfirmation = insertVitamins(vitamins, foodId);
			}
			
			conn.commit();
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
	public boolean update(Food object, int id) throws InfraException {
		PreparedStatement ps = null;
		boolean updateConfirmation = false;
		
		try {
			Food food = retrieveById(id);
			
			if(food == null) {
				updateConfirmation = insert(object);
			}
			
			else {
				ps = conn.prepareStatement("UPDATE Food SET food_name = ?, food_group = ?, calories = ?, proteins = ?, "
										+ "carbohydrates = ?, lipids = ?, fibers = ?, portion = ?, portion_unit = ? WHERE " + 
										"food_id = ?");
				conn.setAutoCommit(false);
				
				ps.setString(1, object.getName());
				ps.setInt(2, object.getFoodGroup().ordinal());
				ps.setFloat(3, object.getCalories());
				ps.setFloat(4, object.getProteins());
				ps.setFloat(5, object.getCarbohydrates());
				ps.setFloat(6, object.getLipids());
				ps.setFloat(7, object.getFibers());
				ps.setFloat(8, object.getPortion());
				ps.setString(9, object.getPortionUnit());
				
				int foodId = retrieveId(object);
				
				ps.setInt(10, foodId);
				
				int rowsAffected = ps.executeUpdate();
				
				if(rowsAffected > 0) {
					updateConfirmation = updateVitamins(new HashMap<>(object.getVitamins()), foodId);
				}
				
				conn.commit();
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to update food registration");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to update food registration and roll back changed data");
			}
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return updateConfirmation;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean delete(Food object) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			int foodId = retrieveId(object);
			
			if(foodId < 0) {
				throw new InfraException("Unable to delete the food: food not found");
			}
			
			ps = conn.prepareStatement("DELETE FROM Vitamin WHERE food_id = ?");
			conn.setAutoCommit(false);
			ps.setInt(1, foodId);
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rowsAffected = -1;
				ps = conn.prepareStatement("DELETE FROM Food WHERE food_id = ?"); 
				ps.setInt(1, foodId);
				
				rowsAffected = ps.executeUpdate();
			}
			
			conn.commit();
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to delete the food");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to delete the food and roll back changed data");
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
}
