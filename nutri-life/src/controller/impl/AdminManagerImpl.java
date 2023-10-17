package controller.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.AdminManager;
import controller.exceptions.ExceptionEntityNotFound;
import controller.exceptions.ExceptionPassword;
import controller.exceptions.ExceptionRegister;
import model.Admin;
import persistence.Persistence;
import persistence.db.exception.InfraException;
import persistence.impl.FactoryAdmin;

public class AdminManagerImpl implements AdminManager {
	private static FactoryAdmin fa;
	private static Persistence<Admin> persistence;

    public AdminManagerImpl() throws InfraException {
		fa = new FactoryAdmin();
		persistence = fa.getPersistence();
    }
    
	private void validateUsername(String username) throws ExceptionRegister {
		if (username.length() > 12) {
			throw new ExceptionRegister("Login must not be longer than 12 characters.");
		} else if (username.isEmpty()) {
			throw new ExceptionRegister("Login must not be empty.");
		} else if (username.matches(".*\\d.*")) {
			throw new ExceptionRegister("Login must not contain numbers.");
		}
	}

	private void validatePassword(String password) throws ExceptionRegister {
		if (password.length() < 8 || password.length() > 20) {
			throw new ExceptionRegister("Password length must be between 8 and 20.");
		}

		Pattern pattern = Pattern.compile("\\d");
		Matcher matcher = pattern.matcher(password);

		int count = 0;
		while (matcher.find()) {
			count++;
		}

		if (count < 2) {
			throw new ExceptionRegister("The password must have at least 2 numbers.");
		}

		boolean containsLetters = false;
		for (char c : password.toCharArray()) {
			if (Character.isLetter(c)) {
				containsLetters = true;
				break;
			}
		}

		if (!containsLetters) {
			throw new ExceptionRegister("The password must have at least 1 letter.");
		}
	}

    @Override
	public boolean insert(String name, String email, String username, String password) throws InfraException, ExceptionRegister {
		validateUsername(username);
		validatePassword(password);
		
		Admin admin = new Admin(name, email, username, password);
		return persistence.insert(admin);
    }

    @Override
    public Admin retrieve(String username, String password) throws ExceptionEntityNotFound, ExceptionPassword, InfraException {
    	Admin admin = new Admin();
    	admin.setUsername(username);
    	admin.setPassword(password);
    	
        Admin aux = persistence.retrieve(admin);
        
        if(aux == null) {
        	throw new ExceptionEntityNotFound("Admin not found");
        }
        
        return aux;
    }

    @Override
    public void generateReport() throws InfraException {
        System.out.println("Implementar Relatorio");
    }
}