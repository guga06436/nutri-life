package controller.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.NutritionistManager;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import model.Nutritionist;
import model.Patient;
import persistence.Persistence;
import persistence.db.exception.InfraException;
import persistence.impl.FactoryNutritionist;
import service.LogService;
import service.impl.LogAdapter;

public class NutritionistManagerImpl implements NutritionistManager{
	private static final LogService log = LogAdapter.getInstance();
	private static FactoryNutritionist fn;
	private static Persistence<Nutritionist> persistence;
	
	public NutritionistManagerImpl() throws InfraException {
		try {
			fn = new FactoryNutritionist();
			persistence = fn.getPersistence();
		}
		catch(InfraException e) {
			log.logException(e);
			throw e;
		}
	}

	@Override
	public boolean add(String name, int age, String crn, String username, String password) throws RegisterException, InfraException {
		try {
			validateAge(age);
			validateUsername(username);
			validatePassword(password);
	
			Nutritionist n = new Nutritionist(name, age, crn, username, password, new ArrayList<Patient>());
			return persistence.insert(n);
		}
		catch(RegisterException e) {
			log.logException(e);
			throw e;
		}
		catch(InfraException e) {
			log.logException(e);
			throw e;
		}
	}

	private void validateAge(int age) throws RegisterException {
		if (age < 18) {
			throw new RegisterException("Age must be equal to or above 18.");
		}
	}

	private void validateUsername(String username) throws RegisterException {
		if (username.isEmpty()) {
			throw new RegisterException("Login must not be empty.");
		} else if (username.length() > 12 || username.matches(".*\\d.*")) {
			throw new RegisterException("Login must not be longer than 12 characters or contain numbers.");
		}
	}

	private void validatePassword(String password) throws RegisterException {
		if (password.length() < 8 || password.length() > 20) {
			throw new RegisterException("Password length must be between 8 and 20.");
		}

		if (password.chars().filter(Character::isDigit).count() < 2) {
			throw new RegisterException("The password must have at least 2 numbers.");
		}

		if (password.chars().noneMatch(Character::isLetter)) {
			throw new RegisterException("The password must have at least 1 letter.");
		}

		Pattern pattern = Pattern.compile("\\d");
		Matcher matcher = pattern.matcher(password);

		int count = 0;
		while (matcher.find()) {
			count++;
		}

		if(count < 2) {
			throw new RegisterException("The password must have at least 2 numbers");
		}

		boolean containsLetters = false;

		for (char c : password.toCharArray()) {
			if (Character.isLetter(c)) {
				containsLetters = true;
				break; // Se encontrar uma letra, sai do loop
			}
		}

		if (!containsLetters) {
			throw new RegisterException("The password must have at least 1 letter");
		}
	}

	@Override
	public Nutritionist retrieve(String username, String password) throws InfraException, EntityNotFoundException {
		try {
			Nutritionist n = new Nutritionist();
			n.setUsername(username);
			n.setPassword(password);
			
			Nutritionist nutritionist = persistence.retrieve(n);
	
			if (nutritionist == null) {
				String message = "Nutritionist not found";
				
				log.logDebug(message + "[username: " + username + "] [password: " + password + "]");
				throw new EntityNotFoundException(message);
			}
	
			return nutritionist;
		}
		catch(InfraException e) {
			log.logException(e);
			throw e;
		}
	}

	@Override
	public List<Nutritionist> listAll() {
		// TODO Auto-generated method stub
		return null;
	}
}