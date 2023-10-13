package controller.impl;

import controller.NutritionistManager;
import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import controller.exceptions.ExceptionRegister;
import model.Nutritionist;
import persistence.NutritionistPersistence;
import persistence.db.exception.InfraException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NutritionistManagerImpl implements NutritionistManager{
	private final NutritionistPersistence np;
	
	public NutritionistManagerImpl() throws InfraException {
		np = new NutritionistPersistence();
	}

	@Override
	public boolean add(String name, int age, String crn, String username, String password) throws InfraException, ExceptionRegister {

		validateAge(age);
		validateUsername(username);
		validatePassword(password);

		Nutritionist n = new Nutritionist(name, age, crn, username, password);
		return np.add(n);
	}

	private void validateAge(int age) throws ExceptionRegister {
		if (age < 18) {
			throw new ExceptionRegister("Age must be equal to or above 18.");
		}
	}

	private void validateUsername(String username) throws ExceptionRegister {
		if (username.isEmpty()) {
			throw new ExceptionRegister("Login must not be empty.");
		} else if (username.length() > 12 || username.matches(".*\\d.*")) {
			throw new ExceptionRegister("Login must not be longer than 12 characters or contain numbers.");
		}
	}

	private void validatePassword(String password) throws ExceptionRegister {
		if (password.length() < 8 || password.length() > 20) {
			throw new ExceptionRegister("Password length must be between 8 and 20.");
		}

		if (password.chars().filter(Character::isDigit).count() < 2) {
			throw new ExceptionRegister("The password must have at least 2 numbers.");
		}

		if (password.chars().noneMatch(Character::isLetter)) {
			throw new ExceptionRegister("The password must have at least 1 letter.");
		}

		Pattern pattern = Pattern.compile("\\d");
		Matcher matcher = pattern.matcher(password);

		int count = 0;
		while (matcher.find()) {
			count++;
		}

		if(count < 2) {
			throw new ExceptionRegister("The password must have at least 2 numbers");
		}

		boolean containsLetters = false;

		for (char c : password.toCharArray()) {
			if (Character.isLetter(c)) {
				containsLetters = true;
				break; // Se encontrar uma letra, sai do loop
			}
		}

		if (!containsLetters) {
			throw new ExceptionRegister("The password must have at least 1 letter");
		}
	}

	@Override
	public Nutritionist retrieve(String login, String password) throws InfraException, ExceptionNotFound, ExceptionPassword {

		Nutritionist n = np.retrieve(login, password);

		if (n == null) {
			throw new ExceptionNotFound("Nutritionist not found");
		}

		if (!n.getPassword().equals(password)) {
			throw new ExceptionPassword("Invalid password");
		}

		return n;
	}
}