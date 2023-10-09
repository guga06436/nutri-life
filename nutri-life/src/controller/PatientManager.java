package controller;

import model.Patient;
import persistence.db.exception.InfraException;

public interface PatientManager {
	boolean add(Patient n) throws InfraException;
	void listAll() throws InfraException;
	Patient retrieve(String login, String password);
}