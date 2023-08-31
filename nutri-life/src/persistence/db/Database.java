package persistence.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mysql.jdbc.Connection;

public class Database {
	private static Connection conn;
	
	private Database() {	
	}
	
	public static Connection getConnection() {
		
		if(conn == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("url");
				conn = (Connection) DriverManager.getConnection(url, props);
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		return conn;
	}
	
	private static Properties loadProperties() {
		try(FileInputStream fis = new FileInputStream("db.properties")){
			Properties props = new Properties();
			props.load(fis);
			
			return props;
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void closeStatement(Statement st) {
		
		try {
			if(st != null) {
				st.close();
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		try {
			if(rs != null) {
				rs.close();
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
