package controller.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.PatientManager;
import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import model.Patient;
import persistence.Persistence;
import persistence.db.exception.InfraException;
import persistence.impl.FactoryPatient;
import service.LogService;
import service.impl.LogAdapter;

public class PatientManagerImpl implements PatientManager{
	private static final LogService log = LogAdapter.getInstance();
	private static FactoryPatient fp;
	private static Persistence<Patient> persistence;
	
	public PatientManagerImpl() throws InfraException {
		try {
			fp = new FactoryPatient();
			persistence = fp.getPersistence();
		}
		catch(InfraException e) {
			log.logException(e);
			throw e;
		}
	}

	@Override
	public boolean add(String username , String password, String name, String cpf, int age, float height, float weight) throws InfraException, RegisterException {
		try {
			validateUsername(username);
			validatePassword(password);
			validateAge(age);
	
			Patient p = new Patient(username, password, name, cpf, age, height, weight);
			return persistence.insert(p);
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

	private void validateAge(int age) throws RegisterException {
		if (age < 18) {
			throw new RegisterException("Age must be equal or above 18.");
		}
	}

	@Override
	public void listAll() throws InfraException {
		try {
			List<Patient> patients = persistence.listAll();
			
			for(Patient p : patients) {
				System.out.println(p);
			}
		}
		catch(InfraException e) {
			log.logException(e);
			throw e;
		}
	}

	@Override
	public Patient retrieve(String username, String password) throws InfraException, EntityNotFoundException {
		Patient p = new Patient();
		
		try {
			p.setUsername(username);
			p.setPassword(password);
			
			Patient patient = persistence.retrieve(p);
	
			if (patient == null) {
				String message = "Nutritionist not found";
				
				log.logDebug(message + " [username: " + username + "] [password: " + password + "]");
				throw new EntityNotFoundException(message);
			}
	
			return p;
		}
		catch(InfraException e) {
			log.logException(e);
			throw e;
		}
	}
}