package persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
	
	private Admin instantiateAdmin(ResultSet rs) throws SQLException {
		Admin admin = new Admin();
		
		admin.setName(rs.getString("admin_name"));
		admin.setUsername(rs.getString("username"));
		admin.setPassword(rs.getString("admin_password"));
		
		return admin;
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
	public Admin retrieve(Admin object) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Admin admin = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM SystemAdministrator WHERE username = ? AND admin_password = ?");
			
			ps.setString(1, object.getUsername());
			ps.setString(2, object.getPassword());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				admin = instantiateAdmin(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve an admin");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve an admin: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return admin;
	}

	@Override
	public List<Admin> listAll() throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Admin> allAdmin = null;
		
		try {
			allAdmin = new ArrayList<>();
			ps = conn.prepareStatement("SELECT * FROM SystemAdministrator");
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				allAdmin.add(instantiateAdmin(rs));
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve all administrators");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return allAdmin;
	}

	@Override
	public int retrieveId(Admin object) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int adminId = -1;
		
		try {
			ps = conn.prepareStatement("SELECT admin_id FROM SystemAdministrator WHERE admin_name = ? AND username = ?");
			
			ps.setString(1, object.getName());
			ps.setString(2, object.getUsername());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				adminId = rs.getInt(1);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve admin ID");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve admin ID: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
	
		return adminId;
	}

	@Override
	public boolean update(Admin object, int id) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			ps = conn.prepareStatement("UPDATE SystemAdministrator SET admin_name = ?, username = ?, admin_password = ? "
										+ "WHERE admin_id = ?");
			
			ps.setString(1, object.getName());
			ps.setString(2, object.getUsername());
			ps.setString(3,  object.getPassword());
			ps.setInt(4, id);
			
			rowsAffected = ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new InfraException("Unable to update system administrator information");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to update system administrator information: null argument in method call");
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
	public boolean delete(Admin object) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1;
		
		try {
			ps = conn.prepareStatement("DELETE FROM SystemAdministrator WHERE username = ? AND admin_password = ?");
			ps.setString(1, object.getUsername());
			ps.setString(2, object.getPassword());
			
			rowsAffected = ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new InfraException("Cannot delete a system administrator");
		}
		catch(NullPointerException e) {
			throw new InfraException("Cannot delete a system administrator: null argument in method call");
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
	public Admin retrieveById(int id) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Admin admin = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM SystemAdministrator WHERE admin_id = ?");
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				admin = instantiateAdmin(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a system administrator");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return admin;
	}
}
