package persistence.impl;

import java.sql.ResultSet;

import com.mysql.jdbc.Connection;

import model.Recipe;
import persistence.db.Database;
import persistence.db.exception.InfraException;

public class RecipePersistence {
	private static Connection conn;
	
	public RecipePersistence() throws InfraException {
		conn = Database.getConnection();
	}
	
	private Recipe instantiateRecipe(ResultSet rs) {
		Recipe recipe = new Recipe();
		
		recipe.setName(rs.getString("recipe_name"));
		
		// Terminar persistência com a inserção do alimento
	}
}
