package controller.impl;

import java.util.List;

import controller.PatientManager;
import controller.exceptions.DatabaseException;
import model.Patient;
import persistence.PatientPersistence;
import persistence.db.exception.InfraException;

public class PatientManagerImpl implements PatientManager{
	private PatientPersistence pp;
	
	public PatientManagerImpl() {
		try {
			pp = new PatientPersistence();
		}
		catch(InfraException e) {
			throw new DatabaseException("Could not connect to the database."); 
		}
	}
	
	@Override
	public boolean add(Patient n) {
		try {
			return pp.add(n);
		}
		catch(InfraException e) {
			throw new DatabaseException("Unable to create a patient.");
		}
	}

	@Override
	public void listAll() {
		try {
			List<Patient> patients = pp.listAll();
			
			for(Patient p : patients) {
				System.out.println(p);
			}
		}
		catch(InfraException e) {
			throw new DatabaseException("Unable to show patients.");
		}
	}

	@Override
	public Patient retrieve(String login, String password) {

		/*usado pro getlogin*/
		return null;
	}
}