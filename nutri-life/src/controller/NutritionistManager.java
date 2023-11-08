package controller;

import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import controller.exceptions.UpdateException;
import model.Nutritionist;
import model.Patient;
import persistence.db.exception.InfraException;

public interface NutritionistManager {
	boolean add(String name, int age, String crn, String username, String password) throws InfraException, RegisterException;
	void updatePatients(Nutritionist nutritionist, Patient patient) throws UpdateException, InfraException;
	Nutritionist retrieve(String username, String password) throws EntityNotFoundException, InfraException;
	void listAll() throws InfraException;
}