package controller;

import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import model.Nutritionist;
import model.Patient;
import persistence.db.exception.InfraException;

public interface PatientManager {

    boolean add(String username , String password, String name, String cpf, int age, float height, float weight, Nutritionist nutritionist) throws InfraException, RegisterException;

    void listAll() throws InfraException;
	Patient retrieve(String username, String password) throws InfraException, EntityNotFoundException;
}