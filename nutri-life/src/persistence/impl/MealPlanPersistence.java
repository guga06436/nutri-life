package persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import model.MealPlan;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class MealPlanPersistence {
	private static Connection conn;
	
	public MealPlanPersistence() throws InfraException {
		conn = Database.getConnection();
	}
	
	private MealPlan instantiateMealPlan(ResultSet rs) throws SQLException{
		MealPlan mealPlan = new MealPlan();
		
		mealPlan.setName(rs.getString("mealplan_name"));
		mealPlan.setCreationDate(new Date(rs.getTimestamp("date_creation").getTime()));
		mealPlan.setGoals(rs.getString("goals"));
		
		// Criar lista de receitas do MealPlan

		return mealPlan;
	}
}
