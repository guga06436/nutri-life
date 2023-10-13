package service;

import model.*;
import controller.*;
import controller.impl.*;
import controller.exceptions.*;
import persistence.db.exception.InfraException;

public class Facade {
    private static Facade instance = null;
    private final NutritionistManager nutritionistManager;
    private final PatientManager patientManager;

    private Facade() throws InfraException {
        nutritionistManager = new NutritionistManagerImpl();
        patientManager = new PatientManagerImpl();
    }

    public static Facade getInstance() throws InfraException {
        if (instance == null) {
            instance = new Facade();
        }
        return instance;
    }

    public boolean addNutritionist(String name, int age, String crn, String username, String password) throws InfraException, ExceptionRegister {
        try {
            return nutritionistManager.add(name, age, crn, username, password);
        } catch (InfraException e) {
            // Handle exceptions if necessary
            e.printStackTrace();
            throw new InfraException(e.getMessage());
        }
    }

    public Nutritionist retrieveNutritionist(String login, String password) throws InfraException, ExceptionLogin {
        try {
            return nutritionistManager.retrieve(login, password);
        } catch (ExceptionNotFound | ExceptionPassword e) {
            // Handle exceptions if necessary
            e.printStackTrace();
            throw new ExceptionLogin(e.getMessage());
        } catch (InfraException e) {
            e.printStackTrace();
            throw new InfraException(e.getMessage());
        }
    }

    public boolean addPatient(String username , String password, String name, String cpf, int age, float height, float weight) throws ExceptionRegister, InfraException {
        try {
            return patientManager.add(username , password, name, cpf, age, height, weight);
        } catch (InfraException e) {
            e.printStackTrace();
            throw new InfraException(e.getMessage());
        }
    }

    public Patient retrievePatient(String login, String password) throws InfraException, ExceptionLogin {
        try {
            return patientManager.retrieve(login, password);
        } catch (ExceptionNotFound | ExceptionPassword e) {
            // Handle exceptions if necessary
            e.printStackTrace();
            throw new ExceptionLogin(e.getMessage());
        } catch (InfraException e) {
            e.printStackTrace();
            throw new InfraException(e.getMessage());
        }
    }

    public void listAllPatients() {
        try {
            patientManager.listAll();
        } catch (InfraException e) {
            e.printStackTrace();
        }
    }
}
