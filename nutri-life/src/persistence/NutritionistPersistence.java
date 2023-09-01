package persistence;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import model.Nutritionist;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class NutritionistPersistence {
	private Connection conn;
	
	public NutritionistPersistence() throws InfraException{
		conn = Database.getConnection();
	}
	
	public boolean add(Nutritionist n) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;

		try {
			ps = (PreparedStatement) conn.prepareStatement("INSERT INTO Nutricionist(name, age, crn, username, password)" + 
															" VALUES(?,?,?,?,?)");

			ps.setString(1, n.getName());
			ps.setInt(2, n.getAge());
			ps.setString(3, n.getCrn());
			ps.setString(4, n.getUsername());
			ps.setString(5, n.getPassword());

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
}