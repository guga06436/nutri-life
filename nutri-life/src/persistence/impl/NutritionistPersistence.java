package persistence.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Nutritionist;
import persistence.Persistence;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class NutritionistPersistence implements Persistence<Nutritionist>{
	private static Connection conn;
	
	public NutritionistPersistence() throws InfraException {
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
	
	@Override
	public boolean insert(Nutritionist n) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;

		try {
			ps = conn.prepareStatement("INSERT INTO Nutritionist(nutritionist_name, age, crn, username, nutritionist_password)" + 
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
			throw new InfraException("Unable to create a nutritionist.");
		}
		finally {
			Database.closeStatement(ps);
		}
		
		return false;
	}
	
	@Override
	public Nutritionist retrieve(Nutritionist nutritionist) throws InfraException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		Nutritionist nutricionist = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM Nutritionist WHERE username = ? AND "
															+	"nutritionist_password = ?");
			
			ps.setString(1, username);
			ps.setString(2, password);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				nutricionist = instantiateNutritionist(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException(e.getMessage()); // Change Exception
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return nutricionist;
	}

	@Override
	public boolean update(Nutritionist object) throws InfraException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Nutritionist delete(Nutritionist object) throws InfraException {
		// TODO Auto-generated method stub
		return null;
	}
}