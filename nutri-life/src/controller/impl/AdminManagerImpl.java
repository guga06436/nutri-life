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
import service.LogService;
import service.impl.LogAdapter;

public class AdminManagerImpl implements AdminManager {
	private static FactoryAdmin fa;
	private static Persistence<Admin> persistence;
	private static final LogService log = LogAdapter.getInstance();

    public AdminManagerImpl() throws InfraException {
    	try {
			fa = new FactoryAdmin();
			persistence = fa.getPersistence();
    	}
    	catch(InfraException e) {
    		log.logException(e);
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
	public boolean insert(String name, String username, String password) throws RegisterException, InfraException {
		try {
			validateUsername(username);
			validatePassword(password);
			
			Admin admin = new Admin(name, username, password);
			
			return persistence.insert(admin);
		} catch (RegisterException e) {
			log.logException(e);
			throw e;
		}
		catch (InfraException e) {
			log.logException(e);
			throw e;
		}
    }

    @Override
    public Admin retrieve(String username, String password) throws EntityNotFoundException, InfraException{
    	Admin admin = new Admin();
    	admin.setUsername(username);
    	admin.setPassword(password);
    	
    	Admin aux = null;
    	
    	try {
			aux = persistence.retrieve(admin);
			
	        if(aux == null) {
	        	String message = "Admin not found";

				log.logDebug(message + ": [username: " + username + "] [password: " + password + "]");
	        	throw new EntityNotFoundException(message);
	        }
		} catch (InfraException e) {
			log.logException(e);
			throw e;
		}

        return aux;
    }
}