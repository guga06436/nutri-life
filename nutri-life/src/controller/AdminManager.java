package controller;

import controller.exceptions.EntityNotFoundException;
import controller.exceptions.RegisterException;
import model.Admin;
import persistence.db.exception.InfraException;

public interface AdminManager {
    boolean insert(String name, String email, String username, String password) throws InfraException, RegisterException;
    Admin retrieve(String username, String password) throws EntityNotFoundException, InfraException;
    void generateReport() throws InfraException;
}