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
	public boolean insert(Patient p) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;

		try {
			ps = conn.prepareStatement("INSERT INTO Patient(patient_name, age, cpf, height, weight, "+ 
															"username, patient_password) VALUES(?,?,?,?,?,?,?)");

			ps.setString(1, p.getName());
			ps.setInt(2, p.getAge());
			ps.setString(3, p.getCpf());
			ps.setFloat(4, p.getHeight());
			ps.setFloat(5, p.getWeight());
			ps.setString(6, p.getUsername());
			ps.setString(7, p.getPassword());

			rowsAffected = ps.executeUpdate();

			if(rowsAffected > 0) {
				return true;
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to create a patient.");
		}
		finally {
			Database.closeStatement(ps);
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
		Patient patient = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM Patient WHERE username = ? AND patient_password = ?");
			
			ps.setString(1, username);
			ps.setString(2, password);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				patient = instantiatePatient(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException(e.getMessage());
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return patient;
	}

	@Override
	public boolean update(Patient object) throws InfraException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Patient delete(Patient object) throws InfraException {
		// TODO Auto-generated method stub
		return null;
	}
}