package controller;

import model.Patient;

public interface PatientManager {
	boolean add(Patient n);
	void listAll();
	Patient retrieve(String login, String password);
}