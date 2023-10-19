package persistence.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Patient;
import persistence.Persistence;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class PatientPersistence implements Persistence<Patient>{
	private static Connection conn;
	
	public PatientPersistence() throws InfraException{
		conn = Database.getConnection();
	}
	
	private Patient instantiatePatient(ResultSet rs) throws SQLException{
		Patient p = new Patient();
		
		p.setName(rs.getString("patient_name"));
		p.setAge(rs.getInt("age"));
		p.setCpf(rs.getString("cpf"));
		p.setHeight(rs.getFloat("height"));
		p.setWeight(rs.getFloat("weight"));
		p.setUsername(rs.getString("username"));
		p.setPassword(rs.getString("patient_password"));

		return p;
	}
	
	@Override
	public boolean insert(Patient patient) throws InfraException {
		PreparedStatement ps = null;
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
			
			rs = st.executeQuery("SELECT * FROM Patient");
			
			while(rs.next()) {
				patients.add(instantiatePatient(rs));
			}
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
			
			ps.setString(1, patient.getUsername());
			ps.setString(2, patient.getPassword());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				pat = instantiatePatient(rs);
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

	@Override
	public boolean update(Patient object, int id) throws InfraException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Patient object) throws InfraException {
		// TODO Auto-generated method stub
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