package managers.impl;

import entities.Patient;
import managers.PatientManager;
import persistence.PatientPersistence;

public class PatientManagerImpl implements PatientManager{
	
	public PatientManagerImpl() {
		
	}
	
	@Override
	public boolean add(Patient n) {
		return PatientPersistence.patientPersistence.add(n);
	}

	@Override
	public void listAll() {
		for(Patient p : PatientPersistence.patientPersistence) {
			System.out.println(p);
		}
	}

	@Override
	public Patient retrieve(String login, String password) {
		return null;
	}

}
