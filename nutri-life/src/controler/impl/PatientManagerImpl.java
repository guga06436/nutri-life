package controler.impl;

import java.util.List;

import controler.PatientManager;
import model.Patient;
import persistence.PatientPersistence;
import exceptions.ExceptionPassword;
import exceptions.ExceptionNotFound;

public class PatientManagerImpl implements PatientManager{
	private PatientPersistence pp;
	
	public PatientManagerImpl() {
		pp = new PatientPersistence();
	}
	
	@Override
	public boolean add(Patient n) {
		return pp.add(n);
	}

	@Override
	public void listAll() {
		List<Patient> patients = pp.listAll();
		
		for(Patient p : patients) {
			System.out.println(p);
		}
	}

	@Override
	public Patient retrieve(String login, String password) {

		Patient loggedInPatient = pp.retrieve(login, password);

		if (loggedInPatient == null) {
			throw new ExceptionNotFound("Patient not found");
		}

		if (!loggedInPatient.getPassword().equals(password)) {
			throw new ExceptionPassword("Invalid Password");
		}

		return loggedInPatient;

	}
}