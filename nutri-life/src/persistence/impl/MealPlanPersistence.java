package persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import model.MealPlan;
import persistence.Persistence;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class MealPlanPersistence implements Persistence<MealPlan>{
	private static Connection conn;
	
	public MealPlanPersistence() throws InfraException {
		conn = Database.getConnection();
	}
	
	private MealPlan instantiateMealPlan(ResultSet rs) throws SQLException{
		MealPlan mealPlan = new MealPlan();
		
		mealPlan.setPlanName(rs.getString("mealplan_name"));
		mealPlan.setCreationDate(new Date(rs.getTimestamp("date_creation").getTime()));
		mealPlan.setGoals(rs.getString("goals"));
		
		// Criar lista de receitas do MealPlan

		return mealPlan;
	}

	@Override
	public boolean insert(MealPlan object) throws InfraException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MealPlan retrieve(MealPlan object) throws InfraException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(MealPlan object) throws InfraException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MealPlan delete(MealPlan object) throws InfraException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MealPlan> listAll() throws InfraException {
		// TODO Auto-generated method stub
		return null;
	}
}
