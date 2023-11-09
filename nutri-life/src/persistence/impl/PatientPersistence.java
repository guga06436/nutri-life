package persistence.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Nutritionist;
import model.Patient;
import persistence.PatientPersistenceExs;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class PatientPersistence implements PatientPersistenceExs{
	private static Connection conn;
	
	public PatientPersistence() throws InfraException{
		conn = Database.getConnection();
	}
	
	@Override
	public boolean insert(Patient patient) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;

		try {
			ps = conn.prepareStatement("INSERT INTO Patient(patient_name, age, cpf, height, weight, "+ 
															"username, patient_password) VALUES(?,?,?,?,?,?,?)");
			
			ps.setString(1, patient.getName());
			ps.setInt(2, patient.getAge());
			ps.setString(3, patient.getCpf());
			ps.setFloat(4, patient.getHeight());
			ps.setFloat(5, patient.getWeight());
			ps.setString(6, patient.getUsername());
			ps.setString(7, patient.getPassword());

			rowsAffected = ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new InfraException("Unable to create a patient.");
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
	public List<Patient> retrieveMatch(Patient object) throws InfraException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Patient> patients = null;
		
		try {
			patients = new ArrayList<>();
			ps = conn.prepareStatement("SELECT * FROM Patient WHERE patient_name LIKE ?");
			ps.setString(1, "%" + object.getName() + "%");
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				patients.add(instantiatePatient(rs));
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve any patient.");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve a patient: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return patients;
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

	@Override
	public boolean update(Patient object, int id) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			Patient p = retrieveById(id);
			
			if(p != null) {
				ps = conn.prepareStatement("UPDATE Patient SET patient_name = ?, age = ?, cpf = ?, height = ?, "
											+ "weight = ?, username = ?, patient_password = ? WHERE patient_id = ?");
				
				ps.setString(1, object.getName());
				ps.setInt(2, object.getAge());
				ps.setString(3, object.getCpf());
				ps.setFloat(4, object.getHeight());
				ps.setFloat(5, object.getWeight());
				ps.setString(6, object.getUsername());
				ps.setString(7, object.getPassword());
				ps.setInt(8, id);
				
				rowsAffected = ps.executeUpdate();
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to update a patient");
		}
		finally {
			Database.closeStatement(ps);
		}
		
		if(rowsAffected > 0) {
			return true;
		}
		
		return false;
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
			
			ps = conn.prepareStatement("DELETE FROM PatientNutritionist WHERE patient_id = ?");
			conn.setAutoCommit(false);
			
			ps.setInt(1, patientId);
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rowsAffected = -1;
				ps = conn.prepareStatement("DELETE FROM MealPlan WHERE patient_id = ?");
				ps.setInt(1, patientId);
				
				rowsAffected = ps.executeUpdate();
				
				if(rowsAffected > 0) {
					ps = conn.prepareStatement("DELETE FROM Patient WHERE patient_id = ?");
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
	
	private Patient instantiatePatient(ResultSet rs) throws InfraException {
		Patient p = new Patient();
		
		try {
			p.setName(rs.getString("patient_name"));
			p.setAge(rs.getInt("age"));
			p.setCpf(rs.getString("cpf"));
			p.setHeight(rs.getFloat("height"));
			p.setWeight(rs.getFloat("weight"));
			p.setUsername(rs.getString("username"));
			p.setPassword(rs.getString("patient_password"));
		}
		catch(SQLException e) {
			throw new InfraException("Unable to instantiate a patient");
		}
		
		return p;
	}

	@Override
	public Patient retrieve(Patient object) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Patient p = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM Patient WHERE username = ? AND patient_password = ?");
			ps.setString(1, object.getUsername());
			ps.setString(2, object.getPassword());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				p = instantiatePatient(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a patient");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return p;
	}

	@Override
	public Nutritionist retrivePatientNutritionist(Patient patient) throws InfraException {
		FactoryNutritionist fn = new FactoryNutritionist();
		NutritionistPersistence nutritionistPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Nutritionist nutritionist = null;
		
		try {
			int patientId = retrieveId(patient);
			
			if(patientId < 0) {
				throw new InfraException("Patient not found");
			}
			
			ps = conn.prepareStatement("SELECT * FROM PatientNutritionist WHERE patient_id = ?");
			ps.setInt(1, patientId);
			
			rs = ps.executeQuery();
			
			nutritionistPersistence = fn.getPersistence();
			while(rs.next()) {
				nutritionist = nutritionistPersistence.retrieveById(rs.getInt("nutritionist_id"));
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve patient nutritionist");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return nutritionist;
	}
}