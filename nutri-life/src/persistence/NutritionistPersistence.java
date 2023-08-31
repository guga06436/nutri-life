package persistence;

import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import model.Nutritionist;
import persistence.db.Database;

public class NutritionistPersistence {
	private Connection conn;
	
	public NutritionistPersistence() {
		conn = Database.getConnection();
	}
	
	public boolean add(Nutritionist n) {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			ps = (PreparedStatement) conn.prepareStatement("INSERT INTO Nutricionist(name, age, crn) VALUES(?,?,?)");
			
			ps.setString(1, n.getName());
			ps.setInt(2, n.getAge());
			ps.setString(3, n.getCrn());
			
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