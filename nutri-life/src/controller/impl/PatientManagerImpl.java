package controller.impl;

import java.util.List;

import controller.PatientManager;
import model.Patient;
import persistence.PatientPersistence;
import persistence.db.exception.InfraException;

public class PatientManagerImpl implements PatientManager{
	private PatientPersistence pp;
	
	public PatientManagerImpl() throws InfraException {
		pp = new PatientPersistence();
	}
	
	@Override
	public boolean add(Patient n) throws InfraException {
		return pp.add(n);
	}

	@Override
	public void listAll() throws InfraException {
		List<Patient> patients = pp.listAll();
		
		for(Patient p : patients) {
			System.out.println(p);
		}
	}

	@Override
	public Patient retrieve(String login, String password) {

		/*usado pro getlogin*/
		return null;
	}
}