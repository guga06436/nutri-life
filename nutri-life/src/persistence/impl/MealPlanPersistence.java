package persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Meal;
import model.MealPlan;
import model.Nutritionist;
import model.Patient;
import persistence.MealPlanPersistenceExs;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class MealPlanPersistence implements MealPlanPersistenceExs{
	private static Connection conn;
	
	public MealPlanPersistence() throws InfraException {
		conn = Database.getConnection();
	}

	@Override
	public boolean insert(MealPlan object) throws InfraException {
		FactoryPatient factoryPatient = new FactoryPatient();
		FactoryNutritionist factoryNutritionist = new FactoryNutritionist();
		FactoryMeal factoryMeal = new FactoryMeal();
		PatientPersistence patientPersistence = null;
		NutritionistPersistence nutritionistPersistence = null;
		MealPersistence mealPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		
		try {
			ps = conn.prepareStatement("INSERT INTO MealPlan(mealplan_name, date_creation, goals, patient_id, nutritionist_id) "
									+ "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			conn.setAutoCommit(false);
			
			ps.setString(1, object.getPlanName());
			ps.setTimestamp(2, new Timestamp(object.getCreationDate().getTime()));
			ps.setString(3, object.getGoals());
			
			patientPersistence = factoryPatient.getPersistence();
			nutritionistPersistence = factoryNutritionist.getPersistence();
			
			int patientId = patientPersistence.retrieveId(object.getPatient());
			int nutritionistId = nutritionistPersistence.retrieveId(object.getNutritionist());
			
			if(patientId < 0 || nutritionistId < 0) {
				throw new InfraException("Unable to insert a meal plan: inconsistent data");
			}
			
			ps.setInt(4, patientId);
			ps.setInt(5, nutritionistId);
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				mealPersistence = factoryMeal.getPersistence();
				rs = ps.getGeneratedKeys();
				
				if(rs.next()) {
					int mealPlanId = rs.getInt(1);
					
					for(Meal meal : object.getMeals()) {
						rowsAffected = -1;
						
						if(mealPersistence.insert(meal)) {
							int mealId = mealPersistence.retrieveId(meal);
							
							if(mealId < 0) {
								throw new InfraException("Unable to insert a meal plan: inconsistent data");
							}
							
							ps = conn.prepareStatement("INSERT INTO MealMealPlan(mealplan_id, meal_id) VALUES (?, ?)");
							ps.setInt(1, mealPlanId);
							ps.setInt(2, mealId);
							
							rowsAffected = ps.executeUpdate();
						}
						else {
							throw new InfraException("Unable to insert a new meal plan");
						}
					}
				}
			}
			
			conn.commit();
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to insert a meal plan");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to insert a meal plan and roll back changed data");
			}
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to insert a meal plan: null parameter");
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
	public List<MealPlan> retrieveMatch(MealPlan object) throws InfraException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MealPlan> mealPlans = null;
		
		try {
			mealPlans = new ArrayList<>();
			ps = conn.prepareStatement("SELECT * FROM MealPlan WHERE mealplan_name LIKE ?");
			ps.setString(1, "%" + object.getPlanName() + "%");
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				mealPlans.add(instantiateMealPlan(rs));
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve any meal plan.");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve a meal plan: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return mealPlans;
	}

	@Override
	public List<MealPlan> listAll() throws InfraException {
		Statement st = null;
		ResultSet rs = null;
		List<MealPlan> allMealPlans;
		
		try {
			allMealPlans = new ArrayList<>();
			
			st = conn.createStatement();
			
			rs = st.executeQuery("SELECT * FROM MealPlan");
			
			while(rs.next()) {
				MealPlan mealPlan = instantiateMealPlan(rs);
				
				if(mealPlan == null) {
					throw new InfraException("Unable to retrieve all meal plans");
				}
				
				allMealPlans.add(mealPlan);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve all meal plans");
		}
		finally {
			Database.closeStatement(st);
		}
		
		return allMealPlans;
	}

	@Override
	public int retrieveId(MealPlan object) throws InfraException {
		FactoryPatient factoryPatient = new FactoryPatient();
		PatientPersistence patientPersistence = null;
		FactoryNutritionist factoryNutritionist = new FactoryNutritionist();
		NutritionistPersistence nutritionistPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int mealPlanId = -1;
		
		try {
			patientPersistence = factoryPatient.getPersistence();
			nutritionistPersistence = factoryNutritionist.getPersistence();
			
			int patientId = patientPersistence.retrieveId(object.getPatient());
			int nutritionistId = nutritionistPersistence.retrieveId(object.getNutritionist());
			
			ps = conn.prepareStatement("SELECT mealplan_id FROM MealPlan WHERE patient_id = ? AND nutritionist_id = ? AND mealplan_name = ?");
			ps.setInt(1, patientId);
			ps.setInt(2, nutritionistId);
			ps.setString(3, object.getPlanName());
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				mealPlanId = rs.getInt("mealplan_id");
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a meal plan");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve a meal plan: null parameter");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return mealPlanId;
	}
	
	private void removeUntrackedMeals(MealPlan newMealPlan, MealPlan oldMealPlan) throws InfraException {
		FactoryMeal factoryMeal = new FactoryMeal();
		MealPersistence mealPersistence = null;
		List<String> newMealsName = new ArrayList<>();
		
		try {
			for(Meal meal : newMealPlan.getMeals()) {
				newMealsName.add(meal.getName());
			}
			
			mealPersistence = factoryMeal.getPersistence();
			for(Meal meal : oldMealPlan.getMeals()) {
				
				if(!newMealsName.contains(meal.getName())) {
					mealPersistence.delete(meal);
				}
			}
		}
		catch(InfraException e) {
			throw new InfraException("Can't upgrade meals from a meal plan");
		}
	}

	@Override
	public boolean update(MealPlan object, int id) throws InfraException {
		FactoryMeal factoryMeal = new FactoryMeal();
		MealPersistence mealPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		
		try {
			MealPlan mealPlan = retrieveById(id);
			
			if(mealPlan == null) {
				throw new InfraException("Unable to update meal plan");
			}
			
			ps = conn.prepareStatement("UPDATE MealPlan SET mealplan_name = ?, goals = ? WHERE mealplan_id = ?");
			conn.setAutoCommit(false);
			ps.setString(1, object.getPlanName());
			ps.setString(2, object.getGoals());
			ps.setInt(3, id);
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				mealPersistence = factoryMeal.getPersistence();
				
				removeUntrackedMeals(object, mealPlan);
				
				for(Meal meal : object.getMeals()) {
					rowsAffected = -1;
					int mealId = mealPersistence.retrieveId(meal);
					
					if(mealId < 0) {
						mealPersistence.insert(meal);
						
						mealId = mealPersistence.retrieveId(meal);
						
						ps = conn.prepareStatement("INSERT INTO MealMealPlan(mealplan_id, meal_id) VALUES(?, ?)");
						ps.setInt(1, id);
						ps.setInt(2, mealId);
						
						rowsAffected = ps.executeUpdate();
						
						if(rowsAffected < 0) {
							throw new InfraException("Unable to update a meal plan");
						}
					}
					else {
						mealPersistence.update(meal, mealId);
					}
				}
			
				conn.commit();
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to update a meal plan");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to update a meal plan and roll back changed data");
			}
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to update a meal plan: null paramater");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		if(rowsAffected > 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean delete(MealPlan object) throws InfraException {
		MealPersistence mealPersistence = null;
		FactoryMeal factoryMeal = new FactoryMeal();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		
		try {
			int mealPlanId = retrieveId(object);
			
			ps = conn.prepareStatement("DELETE FROM MealMealPlan WHERE mealplan_id = ?");
			ps.setInt(1, mealPlanId);
			conn.setAutoCommit(false);
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rowsAffected = -1;
				
				mealPersistence = factoryMeal.getPersistence();
				for(Meal meal : object.getMeals()) {
					mealPersistence.delete(meal);
				}
				
				ps = conn.prepareStatement("DELETE FROM MealPlan WHERE mealplan_id = ?");
				ps.setInt(1, mealPlanId);
				
				rowsAffected = ps.executeUpdate();
				conn.commit();
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to delete meal plan");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to delete meal plan and roll back changed data");
			}
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to delete a meal plan: null parameter");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		if(rowsAffected > 0) {
			return true;
		}
		
		return false;
	}

	@Override
	public MealPlan retrieveById(int id) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		MealPlan mealPlan = null;
		
		try {			
			ps = conn.prepareStatement("SELECT * FROM MealPlan WHERE mealplan_id = ?");
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				mealPlan = instantiateMealPlan(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve meal plan");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return mealPlan;
	}
	
	private List<Meal> listAllMealsMealPlan(int mealPlanId) throws InfraException{
		MealPersistence mealPersistence;
		FactoryMeal factoryMeal = new FactoryMeal();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Meal> allMealsMealPlan = null;
		
		try {
			ps = conn.prepareStatement("SELECT meal_id FROM MealMealPlan WHERE mealplan_id = ?");
			ps.setInt(1, mealPlanId);
			
			rs = ps.executeQuery();
			
			allMealsMealPlan = new ArrayList<>(); 
			mealPersistence = factoryMeal.getPersistence();
			while(rs.next()) {
				Meal meal = mealPersistence.retrieveById(rs.getInt("meal_id"));
				
				if(meal == null) {
					throw new InfraException("Unable to retrieve a meal from the meal plan");
				}
				
				allMealsMealPlan.add(meal);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a meal plan");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return allMealsMealPlan;
	}
	
	private MealPlan instantiateMealPlan(ResultSet rs) throws InfraException {
		FactoryPatient fp = new FactoryPatient();
		FactoryNutritionist fn = new FactoryNutritionist();
		PatientPersistence patientPersistence = null;
		NutritionistPersistence nutritionistPersistence = null;
		MealPlan mealPlan = new MealPlan();
		
		try {
			patientPersistence = fp.getPersistence();
			nutritionistPersistence = fn.getPersistence();
			
			mealPlan.setPlanName(rs.getString("mealplan_name"));
			mealPlan.setCreationDate(new Date(rs.getTimestamp("date_creation").getTime()));
			mealPlan.setGoals(rs.getString("goals"));
			
			Patient patient = patientPersistence.retrieveById(rs.getInt("patient_id"));
			Nutritionist nutritionist = nutritionistPersistence.retrieveById(rs.getInt("nutritionist_id"));
			
			mealPlan.setMeals(listAllMealsMealPlan(rs.getInt("mealplan_id")));
			
			mealPlan.setPatient(patient);
			mealPlan.setNutritionist(nutritionist);
		}
		catch(SQLException e) {
			throw new InfraException("Unable to instantiate a meal plan");
		}
		
		return mealPlan;
	}

	@Override
	public MealPlan retrieve(MealPlan object) throws InfraException {
		FactoryPatient fp = new FactoryPatient();
		FactoryNutritionist fn = new FactoryNutritionist();
		PatientPersistence patientPersistence = null;
		NutritionistPersistence nutritionistPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MealPlan mealPlan = null;
		
		try {
			patientPersistence = fp.getPersistence();
			nutritionistPersistence = fn.getPersistence();
			
			int patientId = patientPersistence.retrieveId(object.getPatient());
			int nutritionistId = nutritionistPersistence.retrieveId(object.getNutritionist());
			
			ps = conn.prepareStatement("SELECT * FROM MealPlan WHERE patient_id = ? AND nutritionist_id = ? AND mealplan_name = ?");
			ps.setInt(1, patientId);
			ps.setInt(2, nutritionistId);
			ps.setString(3, object.getPlanName());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				mealPlan = instantiateMealPlan(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a meal plan");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve a meal plan: Null parameter");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return mealPlan;
	}

	@Override
	public MealPlan retrieveByPatient(Patient patient) throws InfraException {
		FactoryPatient fp = new FactoryPatient();
		PatientPersistence patientPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MealPlan mealPlan = null;
		
		try {
			patientPersistence = fp.getPersistence();
			
			int patientId = patientPersistence.retrieveId(patient);
			
			if(patientId < 0) {
				throw new NullPointerException();
			}
			
			ps = conn.prepareStatement("SELECT * FROM MealPlan WHERE patient_id = ?");
			ps.setInt(1, patientId);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				mealPlan = instantiateMealPlan(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrive a meal plan");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve meal plan from patient: Null paramenter");
		}
		finally {
			
		}
		
		return mealPlan;
	}
}
