package persistence;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import model.Nutritionist;
import model.Patient;
import persistence.db.Database;

public class PatientPersistence {
	private Connection conn;
	
	public PatientPersistence() {
		conn = Database.getConnection();
	}
	
	public boolean add(Patient p) {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			ps = (PreparedStatement) conn.prepareStatement("INSERT INTO Patient(name, age, cpf, heigth, weight) VALUES(?,?,?,?,?)");
			
			ps.setString(1, p.getName());
			ps.setInt(2, p.getAge());
			ps.setString(3, p.getCpf());
			ps.setFloat(4, p.getHeight());
			ps.setFloat(5, p.getWeight());
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				return true;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return false;
	}
}