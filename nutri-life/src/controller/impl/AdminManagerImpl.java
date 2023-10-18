package controller.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.AdminManager;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import model.Admin;
import persistence.Persistence;
import persistence.db.exception.InfraException;
import persistence.impl.FactoryAdmin;

public class AdminManagerImpl implements AdminManager {
	private static FactoryAdmin fa;
	private static Persistence<Admin> persistence;

    public AdminManagerImpl() throws InfraException {
    	try {
			fa = new FactoryAdmin();
			persistence = fa.getPersistence();
    	}
    	catch(InfraException e) {
    		throw e;
    	}
    }
    
	private void validateUsername(String username) throws RegisterException {
		if (username.length() > 12) {
			throw new RegisterException("Login must not be longer than 12 characters.");
		} else if (username.isEmpty()) {
			throw new RegisterException("Login must not be empty.");
		} else if (username.matches(".*\\d.*")) {
			throw new RegisterException("Login must not contain numbers.");
		}
	}

	private void validatePassword(String password) throws RegisterException {
		if (password.length() < 8 || password.length() > 20) {
			throw new RegisterException("Password length must be between 8 and 20.");
		}

		Pattern pattern = Pattern.compile("\\d");
		Matcher matcher = pattern.matcher(password);

		int count = 0;
		while (matcher.find()) {
			count++;
		}

		if (count < 2) {
			throw new RegisterException("The password must have at least 2 numbers.");
		}

		boolean containsLetters = false;
		for (char c : password.toCharArray()) {
			if (Character.isLetter(c)) {
				containsLetters = true;
				break;
			}
		}

		if (!containsLetters) {
			throw new RegisterException("The password must have at least 1 letter.");
		}
	}

    @Override
	public boolean insert(String name, String email, String username, String password) throws InfraException, RegisterException {
		validateUsername(username);
		validatePassword(password);
		
		Admin admin = new Admin(name, email, username, password);
		return persistence.insert(admin);
    }

    @Override
    public Admin retrieve(String username, String password) throws EntityNotFoundException, InfraException {
    	Admin admin = new Admin();
    	admin.setUsername(username);
    	admin.setPassword(password);
    	
        Admin aux = persistence.retrieve(admin);
        
        if(aux == null) {
        	throw new EntityNotFoundException("Admin not found");
        }
        
        return aux;
    }

    @Override
    public void generateReport() throws InfraException {
        System.out.println("Implementar Relatorio");
    }
}