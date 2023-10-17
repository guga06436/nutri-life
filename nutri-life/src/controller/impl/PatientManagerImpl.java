package controller.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.PatientManager;
import controller.exceptions.ExceptionEntityNotFound;
import controller.exceptions.ExceptionPassword;
import controller.exceptions.ExceptionRegister;
import model.Patient;
import persistence.Persistence;
import persistence.db.exception.InfraException;
import persistence.impl.FactoryPatient;

public class PatientManagerImpl implements PatientManager{
	private static FactoryPatient fp;
	private static Persistence<Patient> persistence;
	
	public PatientManagerImpl() throws InfraException {
		fp = new FactoryPatient();
		persistence = fp.getPersistence();
	}

	@Override
	public boolean add(String username , String password, String name, String cpf, int age, float height, float weight) throws InfraException, ExceptionRegister {

		validateUsername(username);
		validatePassword(password);
		validateAge(age);

		Patient p = new Patient(username, password, name, cpf, age, height, weight);
		return persistence.insert(p);
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

	private void validateAge(int age) throws ExceptionRegister {
		if (age < 18) {
			throw new ExceptionRegister("Age must be equal or above 18.");
		}
	}

	@Override
	public void listAll() throws InfraException {
		List<Patient> patients = persistence.listAll();
		
		for(Patient p : patients) {
			System.out.println(p);
		}
	}

	@Override
	public Patient retrieve(String username, String password) throws InfraException, ExceptionEntityNotFound, ExceptionPassword {
		Patient p = new Patient();
		
		p.setUsername(username);
		p.setPassword(password);
		
		Patient patient = persistence.retrieve(p);

		if (patient == null) {
			throw new ExceptionEntityNotFound("Nutritionist not found");
		}

		return p;
	}
}