package persistence.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
	public boolean insert(Nutritionist nutritionist) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;

		try {
			ps = conn.prepareStatement("INSERT INTO Nutritionist(nutritionist_name, age, crn, username, nutritionist_password)" + 
															" VALUES(?,?,?,?,?)");

			ps.setString(1, nutritionist.getName());
			ps.setInt(2, nutritionist.getAge());
			ps.setString(3, nutritionist.getCrn());
			ps.setString(4, nutritionist.getUsername());
			ps.setString(5, nutritionist.getPassword());

			rowsAffected = ps.executeUpdate();

			if(rowsAffected > 0) {
				return true;
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to create a nutritionist.");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to find a nutritionist: null argument in method call");
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
		Nutritionist nutri = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM Nutritionist WHERE username = ? AND "
															+	"nutritionist_password = ?");
			
			ps.setString(1, nutritionist.getUsername());
			ps.setString(2, nutritionist.getPassword());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				nutri = instantiateNutritionist(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a nutritionist"); // Change Exception
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to find a nutritionist: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return nutri;
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

	@Override
	public List<Nutritionist> listAll() throws InfraException {
		// TODO Auto-generated method stub
		return null;
	}
}