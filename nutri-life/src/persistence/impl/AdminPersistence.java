package persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import model.Admin;
import persistence.Persistence;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class AdminPersistence implements Persistence<Admin>{	
	private static Connection conn;
	
	public AdminPersistence() throws InfraException {
		conn = Database.getConnection();
	}

	@Override
	public boolean insert(Admin object) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			ps = conn.prepareStatement("INSERT INTO SystemAdministrator(admin_name, username, admin_password) " + 
										"VALUES (?, ?, ?)");
			
			ps.setString(1, object.getName());
			ps.setString(2, object.getUsername());
			ps.setString(3, object.getPassword());
			
			rowsAffected = ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new InfraException("Unable to create an admin");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to insert an admin: null argument in method call");
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
	public Admin retrieve(Admin object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Admin object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Admin delete(Admin object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Admin> listAll() throws InfraException {
		// TODO Auto-generated method stub
		return null;
	}

}
