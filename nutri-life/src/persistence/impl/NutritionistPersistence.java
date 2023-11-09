package persistence.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.Nutritionist;
import model.Patient;
import persistence.NutritionistPersistenceExs;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class NutritionistPersistence implements NutritionistPersistenceExs{
	private static Connection conn;
	
	public NutritionistPersistence() throws InfraException {
		conn = Database.getConnection();
	}
	
	private Nutritionist instantiateNutritionist(ResultSet rs) throws InfraException {
		Nutritionist nutri = new Nutritionist();
		

		try {
			nutri.setName(rs.getString("nutritionist_name"));
			nutri.setAge(rs.getInt("age"));
			nutri.setCrn(rs.getString("crn"));
			nutri.setUsername(rs.getString("username"));
			nutri.setPassword(rs.getString("nutritionist_password"));
		} catch (SQLException e) {
			throw new InfraException("Cannot instantiate a nutritionist: SQL Error");
		}
		
		return nutri;
	}
	
	@Override
	public Nutritionist retrieve(Nutritionist object) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Nutritionist nutri = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM Nutritionist WHERE username = ? AND nutritionist_password = ?");
			ps.setString(1, object.getUsername());
			ps.setString(2, object.getPassword());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				nutri = instantiateNutritionist(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a nutritionist");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return nutri;
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
		
		if(rowsAffected > 0) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public List<Nutritionist> retrieveMatch(Nutritionist object) throws InfraException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Nutritionist> nutritionists = null;
		
		try {
			nutritionists = new ArrayList<>();
			ps = conn.prepareStatement("SELECT * FROM Nutritionist WHERE nutritionist_name LIKE ?");
			ps.setString(1, "%" + object.getName() + "%");
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				nutritionists.add(instantiateNutritionist(rs));
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve any nutritionist.");
		}
		catch(NullPointerException e) {
			throw new InfraException("Unable to retrieve a nutritionist: null argument in method call");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return nutritionists;
	}

	@Override
	public List<Nutritionist> listAll() throws InfraException {
		Statement st = null;
		ResultSet rs = null;
		List<Nutritionist> allNutritionist;
		
		try {
			st = conn.createStatement();
			
			rs = st.executeQuery("SELECT * FROM Nutritionist");
			
			allNutritionist = new ArrayList<>();
			while(rs.next()) {
				Nutritionist nutri = instantiateNutritionist(rs);
				
				allNutritionist.add(nutri);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrive all nutritionists");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(st);
		}
		
		return allNutritionist;
	}

	@Override
	public int retrieveId(Nutritionist object) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int nutritionistId = -1;
		
		try {
			ps = conn.prepareStatement("SELECT nutritionist_id FROM Nutritionist WHERE crn = ?");
			ps.setString(1, object.getCrn());
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				nutritionistId = rs.getInt(1);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve nutritionist ID");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return nutritionistId;
	}

	@Override
	public boolean update(Nutritionist object, int id) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowsAffected = -1;
		
		try {
			Nutritionist nutri = retrieveById(id);
			
			if(nutri == null) {
				throw new InfraException("Unable to update a nutritionist");
			}
			
			ps = conn.prepareStatement("UPDATE Nutritionist SET nutritionist_name = ?, age = ?, crn = ?, username = ?, nutritionist_password = ?"
										+ " WHERE nutritionist_id = ?");

			ps.setString(1, object.getName());
			ps.setInt(2, object.getAge());
			ps.setString(3, object.getCrn());
			ps.setString(4, object.getUsername());
			ps.setString(5, object.getPassword());
			ps.setInt(6, id);
			
			rowsAffected = ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new InfraException("Unable to update a nutritionist");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		if(rowsAffected > 0) {
			return true;
		}
		
		return false;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean delete(Nutritionist object) throws InfraException {
		PreparedStatement ps = null;
		int rowsAffected = -1; 
		
		try {
			int nutritionistId = retrieveId(object);
			
			if(nutritionistId < 0) {
				throw new InfraException("It's not possible to exclude a nutritionist: he doesn't exist!");
			}
			
			ps = conn.prepareStatement("DELETE FROM Nutritionist WHERE nutritionist_id = ?");
			conn.setAutoCommit(false);
			ps.setInt(1, nutritionistId);
			
			rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rowsAffected = -1;
				
				ps = conn.prepareStatement("UPDATE PatientNutritionist SET nutritionist_id = ? WHERE nutritionist_id = ?");
				ps.setNull(1, Types.NULL);
				ps.setInt(2, nutritionistId);
				
				rowsAffected = ps.executeUpdate();
				
				if(rowsAffected > 0) {
					rowsAffected = -1;
					
					ps = conn.prepareStatement("DELETE FROM MealPlan WHERE nutritionist_id = ?");
					ps.setInt(1, nutritionistId);
					
					rowsAffected = ps.executeUpdate();
					
					conn.commit();
				}
			}
		}
		catch(SQLException e) {
			try {
				conn.rollback();
				throw new InfraException("Unable to exclude nutritionist");
			}
			catch(SQLException r) {
				throw new InfraException("Unable to exclude nutritionist and roll back changed data");
			}
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
	public Nutritionist retrieveById(int id) throws InfraException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Nutritionist nutri = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM Nutritionist WHERE nutritionist_id = ?");
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				nutri = instantiateNutritionist(rs);
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve a nutritionist"); 
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
	public List<Patient> listAllNutritionistPatients(Nutritionist nutritionist) throws InfraException {
		FactoryPatient fp = new FactoryPatient();
		PatientPersistence patientPersistence = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Patient> patients = null;
		
		try {
			patients = new ArrayList<>();
			ps = conn.prepareStatement("SELECT patient_id FROM PatientNutritionist WHERE nutritionist_id IN " + 
										"(SELECT nutritionist_id FROM Nutritionist WHERE crn = ?)");
			ps.setString(1, nutritionist.getCrn());
			
			rs = ps.executeQuery();
			
			patientPersistence = fp.getPersistence();
			while(rs.next()) {
				patients.add(patientPersistence.retrieveById(rs.getInt("patient_id")));
			}
		}
		catch(SQLException e) {
			throw new InfraException("Unable to retrieve patients from nutritionist");
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeStatement(ps);
		}
		
		return patients;
	}
}