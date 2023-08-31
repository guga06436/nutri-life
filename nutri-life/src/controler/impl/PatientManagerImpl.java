package controler.impl;

import java.util.List;

import controler.PatientManager;
import model.Patient;
import persistence.PatientPersistence;

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
		return null;
	}
}