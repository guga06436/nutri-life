package controller;

import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import controller.exceptions.ExceptionRegister;
import model.Patient;
import persistence.db.exception.InfraException;

public interface PatientManager {
	boolean add(String username , String password, String name, String cpf, int age, float height, float weight) throws InfraException, ExceptionRegister;
	void listAll() throws InfraException;
	Patient retrieve(String username, String password) throws InfraException, ExceptionNotFound, ExceptionPassword;
}