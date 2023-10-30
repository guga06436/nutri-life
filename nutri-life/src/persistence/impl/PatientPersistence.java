package persistence.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.MealPlan;
import model.Nutritionist;
import model.Patient;
import persistence.Persistence;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class PatientPersistence implements Persistence<Patient>{
	private static Connection conn;
	
	public PatientPersistence() throws InfraException{
		conn = Database.getConnection();
	}
	
	@SuppressWarnings("resource")
	private Patient instantiatePatient(ResultSet rs) throws SQLException, InfraException{
		FactoryNutritionist factoryNutritionist = new FactoryNutritionist();
		FactoryMealPlan factoryMp = new FactoryMealPlan();
		NutritionistPersistence nutritionistPersistence = null;
		MealPlanPersistence mpPersistence = null;
		PreparedStatement ps = null;
		ResultSet rsAux = null;
		Patient p = null;
		
		try {
			p = new Patient();
			 
			p.setName(rs.getString("patient_name"));
			p.setAge(rs.getInt("age"));
			p.setCpf(rs.getString("cpf"));
			p.setHeight(rs.getFloat("height"));
			p.setWeight(rs.getFloat("weight"));
			p.setUsername(rs.getString("username"));
			p.setPassword(rs.getString("patient_password"));
			
			ps = conn.prepareStatement("SELECT nutritionist_id FROM PatientNutritionist WHERE patient_id = ?");
			conn.setAutoCommit(false);
			ps.setInt(1, rs.getInt("patient_id"));
			
			rsAux = ps.executeQuery();
			
			if(rsAux.next()) {
				int nutritionistId = rsAux.getInt(1);
				
				if(nutritionistId > 0) {
					nutritionistPersistence = factoryNutritionist.getPersistence();
					Nutritionist nutri = nutritionistPersistence.retrieveById(nutritionistId);
					
					if(nutri != null) {
						p.setNutritionist(nutri);
						
						ps = conn.prepareStatement("SELECT mealplan_id FROM MealPlan WHERE patient_id = ? AND nutritionist_id = ?");
						ps.setInt(1, rs.getInt("patient_id"));
						ps.setInt(2, nutritionistId);
						
						rsAux = ps.executeQuery();
						
						if(rsAux.next()) {
							int mealPlanId = rsAux.getInt(1);
							
							if(mealPlanId > 0) {
								mpPersistence = factoryMp.getPersistence();
								MealPlan mealPlan = mpPersistence.retrieveById(mealPlanId);
								
								if(mealPlan != null) {
									p.setMealPlan(mealPlan);
									conn.commit();
								}
							}
						}
					}
				}
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to instantiate a patient object");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to instantiate a patient object and roll back changed data");
			}
		}
		finally {
			Database.closeResultSet(rsAux);
			Database.closeStatement(ps);
		}

		return p;
	}
	
	private int insertNutritionistPatient(int patientId, Patient patient) throws InfraException {
		FactoryNutritionist factoryNutritionist = new FactoryNutritionist();
		NutritionistPersistence nutritionistPersistence = null;
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			nutritionistPersistence = factoryNutritionist.getPersistence();
			
			Integer nutritionistId = null;
			
			if(patient.getNutritionist() != null) {
				nutritionistId = nutritionistPersistence.retrieveId(patient.getNutritionist());
			}
			
			ps = conn.prepareStatement("INSERT INTO PatientNutritionist(patient_id, nutritionist_id) VALUES (?, ?)");
			ps.setInt(1, patientId);
			ps.setInt(2, nutritionistId);
			
			rowsAffected = ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new InfraException("Unable to insert patient information");
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return rowsAffected;
	}
	
	@Override
	public boolean insert(Patient patient) throws InfraException {
		FactoryMealPlan factoryMp = new FactoryMealPlan();
		MealPlanPersistence mpPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;

		try {
			ps = conn.prepareStatement("INSERT INTO Patient(patient_name, age, cpf, height, weight, "+ 
															"username, patient_password) VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			conn.setAutoCommit(false);

			ps.setString(1, patient.getName());
			ps.setInt(2, patient.getAge());
			ps.setString(3, patient.getCpf());
			ps.setFloat(4, patient.getHeight());
			ps.setFloat(5, patient.getWeight());
			ps.setString(6, patient.getUsername());
			ps.setString(7, patient.getPassword());

			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rowsAffected = -1;
				rs = ps.getGeneratedKeys();
				
				while(rs.next()) {
					int patientId = rs.getInt(1);
					
					rowsAffected = insertNutritionistPatient(patientId, patient);
					
					if(rowsAffected > 0) {
						
						mpPersistence = factoryMp.getPersistence();
						if(patient.getMealPlan() != null) {
							int mealPlanId = mpPersistence.retrieveId(patient.getMealPlan());
							
							if(mealPlanId < 0) {
								mpPersistence.insert(patient.getMealPlan());
							}
						}
						
						conn.commit();
					}
				}
				
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to create a patient.");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to create a patient and roll back changed data");
			}
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to insert a patient: null argument in method call");
		}
		finally {
			Database.closeStatement(ps);
			Database.closeResultSet(rs);
		}
		
		if(rowsAffected > 0) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public List<Patient> listAll() throws InfraException{
		List<Patient> patients = new ArrayList<>();
		Statement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.createStatement();
			conn.setAutoCommit(false);
			
			rs = st.executeQuery("SELECT * FROM Patient");
			
			while(rs.next()) {
				patients.add(instantiatePatient(rs));
			}
			
			conn.commit();
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrive all patients.");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(st);
		}
		
		return patients;
	}
	
	@Override
	public Patient retrieve(Patient patient) throws InfraException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		Patient pat = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM Patient WHERE username = ? AND patient_password = ?");
			conn.setAutoCommit(false);
			
			ps.setString(1, patient.getUsername());
			ps.setString(2, patient.getPassword());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				pat = instantiatePatient(rs);
			}
			
			conn.commit();
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a patient");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve a patient: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return pat;
	}

	@Override
	public int retrieveId(Patient object) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int patientId = -1;
		
		try {
			ps = conn.prepareStatement("SELECT patient_id FROM Patient WHERE cpf = ?");
			
			ps.setString(1, object.getCpf());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				patientId = rs.getInt(1);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve patient ID");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve patient ID: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return patientId;
	}
	
	@SuppressWarnings({ "resource", "unused" })
	private int updateNutritionistPatient(Patient patient, int patientId) throws InfraException {
		FactoryNutritionist factoryNutritionist = new FactoryNutritionist();
		NutritionistPersistence nutritionistPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		
		try {
			ps = conn.prepareStatement("SELECT nutritionist_id FROM PatientNutritionist WHERE patient_id = ?");
			conn.setAutoCommit(false);
			ps.setInt(1, patientId);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				Integer nutritionistId = rs.getInt(1);
				
				if(patient.getNutritionist() == null) {
					if(nutritionistId != null) {
						ps = conn.prepareStatement("DELETE FROM PatientNutritionist WHERE nutritionist_id = ?");
						ps.setInt(1, nutritionistId);
						
						rowsAffected = ps.executeUpdate();
					}
				}
				else {
					nutritionistPersistence = factoryNutritionist.getPersistence();
					
					if(nutritionistId == null) {	
						nutritionistId = nutritionistPersistence.retrieveId(patient.getNutritionist());
						
						ps = conn.prepareStatement("INSERT INTO PatientNutritionist(nutritionist_id) VALUES (?) "
								+ "WHERE patient_id = ?");
						
						ps.setInt(1, nutritionistId);
						ps.setInt(2, patientId);
						
						rowsAffected = ps.executeUpdate();
					}
					else {
						int auxNutriId = nutritionistPersistence.retrieveId(patient.getNutritionist());
						
						if(auxNutriId != nutritionistId) {
							ps = conn.prepareStatement("UPDATE PatientNutritionist SET nutritionist_id = ? WHERE patient_id = ?");
							ps.setInt(1, auxNutriId);
							ps.setInt(2, patientId);
							
							rowsAffected = ps.executeUpdate();
						}
					}
				}
				
				conn.commit();
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to update patient information");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to update patient information and roll back changed data");
			}
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return rowsAffected;
	}
	
	@SuppressWarnings("resource")
	private boolean updateMealPlaPatient(Patient object, int patientId) throws InfraException {
		FactoryMealPlan factoryMp = new FactoryMealPlan();
		MealPlanPersistence mpPersistence = null;
		FactoryNutritionist factoryNutritionist = new FactoryNutritionist();
		NutritionistPersistence nutritionistPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean updateConfirmation = false;
		int rowsAffected = -1;
		
		try {
			
			if(object.getNutritionist() != null) {
				mpPersistence = factoryMp.getPersistence();
				nutritionistPersistence = factoryNutritionist.getPersistence();
				
				if(object.getMealPlan() == null) {	
					ps = conn.prepareStatement("SELECT mealplan_id FROM MealPlan WHERE patient_id = ? AND nutritionist_id = ?");
					conn.setAutoCommit(false);
					ps.setInt(1, patientId);
					
					Integer nutritionistId = nutritionistPersistence.retrieveId(object.getNutritionist());
					
					ps.setInt(2, nutritionistId);
					
					rs = ps.executeQuery();
					
					if(rs.next()) {
						int mealPlanId = rs.getInt("mealplan_id");
						
						if(mealPlanId > 0) {
							ps = conn.prepareStatement("DELETE FROM MealPlan WHERE mealplan_id = ?");
							ps.setInt(1, mealPlanId);
							
							rowsAffected = ps.executeUpdate();
							
							if(rowsAffected > 0) {
								updateConfirmation = true;
							}
						}
					}
				}
				else {
					int mealPlanId = mpPersistence.retrieveId(object.getMealPlan());
					
					if(mealPlanId < 0) {
						updateConfirmation = mpPersistence.insert(object.getMealPlan());
					}
					else {
						updateConfirmation = mpPersistence.update(object.getMealPlan(), mealPlanId);
					}
				}
				
				conn.commit();
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to update patient information");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to update patient information and changed data");
			}
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return updateConfirmation;
	}

	@Override
	public boolean update(Patient object, int id) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		boolean updateConfirmation = false;
		
		try {
			Patient p = retrieveById(id);
			
			if(p != null) {
				ps = conn.prepareStatement("UPDATE Patient SET patient_name = ?, age = ?, cpf = ?, height = ?, "
											+ "weight = ?, username = ?, patient_password = ? WHERE patient_id = ?");
				
				conn.setAutoCommit(false);
				
				ps.setString(1, object.getName());
				ps.setInt(2, object.getAge());
				ps.setString(3, object.getCpf());
				ps.setFloat(4, object.getHeight());
				ps.setFloat(5, object.getWeight());
				ps.setString(6, object.getUsername());
				ps.setString(7, object.getPassword());
				ps.setInt(8, id);
				
				rowsAffected = ps.executeUpdate();
				
				if(rowsAffected > 0) {
					rowsAffected = updateNutritionistPatient(object, id);
					
					if(rowsAffected > 0) {
						updateConfirmation = updateMealPlaPatient(object, id);
						conn.commit();
					}
				}
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to update a patient");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to update a patient and roll back changed data");
			}
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return updateConfirmation;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean delete(Patient object) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			int patientId = retrieveId(object);
			
			if(patientId < 0) {
				throw new InfraException("Unable to delete a patient from the database");
			}
			
			ps = conn.prepareStatement("DELETE FROM Patient WHERE patient_id = ?");
			conn.setAutoCommit(false);
			
			ps.setInt(1, patientId);
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rowsAffected = -1;
				ps = conn.prepareStatement("DELETE FROM MealPlan WHERE patient_id = ?");
				ps.setInt(1, patientId);
				
				rowsAffected = ps.executeUpdate();
				
				if(rowsAffected > 0) {
					ps = conn.prepareStatement("DELETE FROM PatientNutritionist WHERE patient_id = ?");
					ps.setInt(1, patientId);
					
					rowsAffected = ps.executeUpdate();	
					
					conn.commit();
				}
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to delete a patient from the database");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to delete a patient from the database and roll back chagend data");
			}
		}
		catch(NullPointerException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to delete a patient from the database: null argument in method call");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to delete a patient from the database and roll back changed data: null argument in method call");
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
	public Patient retrieveById(int id) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Patient patient = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM Patient WHERE patient_id = ?");
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				patient = instantiatePatient(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a patient");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve a patient: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return patient;
	}
}