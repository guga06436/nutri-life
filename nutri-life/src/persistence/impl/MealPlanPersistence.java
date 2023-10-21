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
import model.Recipe;
import persistence.Persistence;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class MealPlanPersistence implements Persistence<MealPlan>{
	private static Connection conn;
	
	public MealPlanPersistence() throws InfraException {
		conn = Database.getConnection();
	}
	
	private List<Meal> listAllMealsMealPlan(int mealPlanId) throws InfraException{
		MealPersistence mealPersistence;
		FactoryMeal factoryMeal = new FactoryMeal();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Meal> allMealsMealPlan = null;
		
		try {
			ps = conn.prepareStatement("SELECT meal_id FROM Meal WHERE mealplan_id = ?");
			
			ps.setInt(1, mealPlanId);
			
			rs = ps.executeQuery();
			
			allMealsMealPlan = new ArrayList<>(); 
			mealPersistence = factoryMeal.getPersistence();
			while(rs.next()) {
				Meal meal = mealPersistence.retrieveById(rs.getInt(1));
				
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
	
	private List<Recipe> listAllRecipesMealPlan(int mealPlanId) throws InfraException{
		RecipePersistence recipePersistence;
		FactoryRecipe factoryRecipe = new FactoryRecipe();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Recipe> allRecipesMealPlan = null;
		
		try {
			ps = conn.prepareStatement("SELECT recipe_id FROM RecipeMealPlan WHERE mealplan_id = ?");
			
			ps.setInt(1, mealPlanId);
			
			rs = ps.executeQuery();
			
			allRecipesMealPlan = new ArrayList<>();
			recipePersistence = factoryRecipe.getPersistence();
			while(rs.next()) {
				Recipe recipe = recipePersistence.retrieveById(rs.getInt(1));
				
				if(recipe == null) {
					throw new InfraException("Unable to retrieve a meal plan");
				}
				
				allRecipesMealPlan.add(recipe);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a meal plan");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return allRecipesMealPlan;
	}
	
	private MealPlan instantiateMealPlan(ResultSet rs, int mealPlanId) throws SQLException, InfraException{
		FactoryPatient factoryPatient = new FactoryPatient();
		PatientPersistence patientPersistence = null;
		FactoryNutritionist factoryNutritionist = new FactoryNutritionist();
		NutritionistPersistence nutritionistPersistence = null;
		MealPlan mealPlan = new MealPlan();
		
		patientPersistence = factoryPatient.getPersistence();
		nutritionistPersistence = factoryNutritionist.getPersistence();
		
		mealPlan.setPlanName(rs.getString("mealplan_name"));
		mealPlan.setCreationDate(new Date(rs.getTimestamp("date_creation").getTime()));
		mealPlan.setGoals(rs.getString("goals"));
		mealPlan.setRecipeList(listAllRecipesMealPlan(mealPlanId));
		mealPlan.setMeals(listAllMealsMealPlan(mealPlanId));
		
		Patient patient = patientPersistence.retrieveById(rs.getInt("patient_id"));
		Nutritionist nutritionist = nutritionistPersistence.retrieveById(rs.getInt("nutritionist_id"));
		
		mealPlan.setPatient(patient);
		mealPlan.setNutritionist(nutritionist);

		return mealPlan;
	}

	@Override
	public boolean insert(MealPlan object) throws InfraException {
		FactoryPatient factoryPatient = new FactoryPatient();
		FactoryNutritionist factoryNutritionist = new FactoryNutritionist();
		FactoryRecipe factoryRecipe = new FactoryRecipe();
		FactoryMeal factoryMeal = new FactoryMeal();
		PatientPersistence patientPersistence = null;
		NutritionistPersistence nutritionistPersistence = null;
		RecipePersistence recipePersistence = null;
		MealPersistence mealPersistence = null;
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			ps = conn.prepareStatement("INSERT INTO MealPlan(mealplan_name, date_creation, goals, patient_id, nutritionist_id) "
									+ "VALUES (?, ?, ?, ?, ?)");
			
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
			
			recipePersistence = factoryRecipe.getPersistence();
			mealPersistence = factoryMeal.getPersistence();
			
			for(Recipe recipe : object.getRecipeList()) {
				int recipeId = recipePersistence.retrieveId(recipe);
				
				if(recipeId < 0) {
					throw new InfraException("Unable to insert a meal plan: inconsistent data");
				}
				
				recipePersistence.insert(recipe);
			}
			
			for(Meal meal : object.getMeals()) {
				int mealId = mealPersistence.retrieveId(meal);
				
				if(mealId < 0) {
					throw new InfraException("Unable to insert a meal plan: inconsistent data");
				}
				
				mealPersistence.insert(meal);
			}
			
			rowsAffected = ps.executeUpdate();		
		}
		catch(SQLException e) {
			throw new InfraException("Unable to insert a meal plan");
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
	public MealPlan retrieve(MealPlan object) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		MealPlan mealPlan = null;
		
		try {
			int mealPlanId = retrieveId(object);
			
			ps = conn.prepareStatement("SELECT * FROM MealPlan WHERE mealplan_id = ?");
			ps.setInt(1, mealPlanId);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				mealPlan = instantiateMealPlan(rs, mealPlanId);
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
				MealPlan mealPlan = instantiateMealPlan(rs, rs.getInt("mealplan_id"));
				
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
			
			ps = conn.prepareStatement("SELECT mealplan_id FROM MealPlan WHERE patient_id = ? AND nutritionist_id = ?");
			ps.setInt(1, patientId);
			ps.setInt(2, nutritionistId);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				mealPlanId = rs.getInt("mealplan_id");
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a meal plan");
		}
		finally {
			
		}
		
		return mealPlanId;
	}

	@Override
	public boolean update(MealPlan object, int id) throws InfraException {
		FactoryRecipe factoryRecipe = new FactoryRecipe();
		FactoryMeal factoryMeal = new FactoryMeal();
		RecipePersistence recipePersistence = null;
		MealPersistence mealPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		
		try {
			MealPlan mealPlan = retrieveById(id);
			
			if(mealPlan == null) {
				throw new InfraException("Unable to update meal plan");
			}
			
			ps = conn.prepareStatement("UPDATE MealPlan SET mealplan_name = ?, goals = ?");
			ps.setString(1, object.getPlanName());
			ps.setString(2, object.getGoals());
			
			recipePersistence = factoryRecipe.getPersistence();
			
			for(Recipe recipe : object.getRecipeList()) {
				int recipeId = recipePersistence.retrieveId(recipe);
				
				if(recipeId < 0) {
					throw new InfraException("Unable to update meal plan information: inconsistent data");
				}
				
				recipePersistence.update(recipe, recipeId);
			}
			
			mealPersistence = factoryMeal.getPersistence();
			
			for(Meal meal : object.getMeals()) {
				int mealId = mealPersistence.retrieveId(meal);
				
				if(mealId < 0) {
					throw new InfraException("Unable to update meal plan information: inconsistent data");
				}
				
				mealPersistence.update(meal, mealId);
			}
			
			rowsAffected = ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new InfraException("Unable to update a meal plan");
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
		RecipePersistence recipePersistence = null;
		FactoryRecipe factoryRecipe = new FactoryRecipe();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		
		try {
			int mealPlanId = retrieveId(object);
			
			ps = conn.prepareStatement("DELETE FROM RecipeMealPlan WHERE mealplan_id = ?");
			ps.setInt(1, mealPlanId);
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rowsAffected = -1;
				
				ps = conn.prepareStatement("SELECT recipe_id FROM Recipe WHERE mealplan_id = ?");
				ps.setInt(1, mealPlanId);
				
				rs = ps.executeQuery();
				
				recipePersistence = factoryRecipe.getPersistence();
				while(rs.next()) {
					Recipe recipe = recipePersistence.retrieveById(rs.getInt("recipe_id"));
					
					if(!recipePersistence.delete(recipe)) {
						throw new InfraException("Unable to delete meal plan information");
					}
				}
				
				ps = conn.prepareStatement("SELECT meal_id FROM Meal WHERE mealplan_id = ?");
				ps.setInt(1, mealPlanId);
				
				rs = ps.executeQuery();
				
				mealPersistence = factoryMeal.getPersistence();
				while(rs.next()) {
					Meal meal = mealPersistence.retrieveById(rs.getInt("meal_id"));
					
					if(!mealPersistence.delete(meal)) {
						throw new InfraException("Unable to delete meal plan information");
					}
				}
				
				ps = conn.prepareStatement("DELETE FROM MealPlan WHERE mealplan_id = ?");
				ps.setInt(1, mealPlanId);
				
				rowsAffected = ps.executeUpdate();
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to delete meal plan");
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
				mealPlan = instantiateMealPlan(rs, id);
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
}
