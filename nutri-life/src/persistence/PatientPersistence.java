package persistence;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import model.Patient;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class PatientPersistence {
	private Connection conn;
	
	public PatientPersistence() throws InfraException{
		conn = Database.getConnection();
	}
	
	private Patient instantiatePatient(ResultSet rs) throws SQLException{
		Patient p = new Patient();
		
		p.setName(rs.getString("name"));
		p.setAge(rs.getInt("age"));
		p.setCpf(rs.getString("cpf"));
		p.setHeight(rs.getFloat("height"));
		p.setWeight(rs.getFloat("weight"));
		p.setUsername(rs.getString("username"));
		p.setPassword(rs.getString("password"));

		return p;
	}
	
	public boolean add(Patient p) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;

		try {
			ps = (PreparedStatement) conn.prepareStatement("INSERT INTO Patient(name, age, cpf, heigth, weight) VALUES(?,?,?,?,?)");
			ps = (PreparedStatement) conn.prepareStatement("INSERT INTO Patient(name, age, cpf, heigth, weight, "+ 
															"username, password) VALUES(?,?,?,?,?,?,?)");

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
			throw new InfraException(e.getMessage());
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return false;
	}
	
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
			throw new InfraException(e.getMessage());
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(st);
		}
		
		return patients;
	}
}