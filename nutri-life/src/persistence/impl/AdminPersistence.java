package persistence.impl;

import java.sql.Connection;
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
	public boolean insert(Admin object) {
		// TODO Auto-generated method stub
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
