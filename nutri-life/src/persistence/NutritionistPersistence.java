package persistence;
import java.sql.ResultSet;
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
	
	private Nutritionist instantiateNutritionist(ResultSet rs) throws SQLException{
		Nutritionist nutritionist = new Nutritionist();
		
		nutritionist.setName(rs.getString("nutritionist_name"));
		nutritionist.setAge(rs.getInt("age"));
		nutritionist.setCrn(rs.getString("crn"));
		nutritionist.setUsername(rs.getString("username"));
		nutritionist.setPassword(rs.getString("nutritionist_password"));

		return nutritionist;
	}	
	
	public boolean add(Nutritionist n) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;

		try {
			ps = (PreparedStatement) conn.prepareStatement("INSERT INTO Nutritionist(nutritionist_name, age, crn, username, nutritionist_password)" + 
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
	
	public Nutritionist retrieve(String username, String password) throws InfraException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		Nutritionist nutricionist = null;
		
		try {
			ps = (PreparedStatement) conn.prepareStatement("SELECT * FROM Nutritionist WHERE username = ? AND "
															+	"nutritionist_password = ?");
			
			ps.setString(1, username);
			ps.setString(2, password);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				nutricionist = instantiateNutritionist(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException(e.getMessage());
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return nutricionist;
	}
}