package persistence.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mysql.jdbc.Connection;

import persistence.db.exception.InfraException;

public class Database {
	private static Connection conn;
	
	private Database() {	
	}
	
	public static Connection getConnection() throws InfraException{
		
		if(conn == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("url");
				conn = (Connection) DriverManager.getConnection(url, props);
			}
			catch(SQLException e) {
				throw new InfraException(e.getMessage());
			}
		}
		
		return conn;
	}
	
	private static Properties loadProperties() throws InfraException{
		try(FileInputStream fis = new FileInputStream("db.properties")){
			Properties props = new Properties();
			props.load(fis);
			
			return props;
		}
		catch(IOException e) {
			throw new InfraException("Database access credentials error.");
		}
	}
	
	public static void closeStatement(Statement st) throws InfraException{
		
		try {
			if(st != null) {
				st.close();
			}
		}
		catch(SQLException e) {
			throw new InfraException("Resource closing error.");
		}
	}
	
	public static void closeResultSet(ResultSet rs) throws InfraException{
		try {
			if(rs != null) {
				rs.close();
			}
		}
		catch(SQLException e) {
			throw new InfraException("Resource closing error.");
		}
	}
}
