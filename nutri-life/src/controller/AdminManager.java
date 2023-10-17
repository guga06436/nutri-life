package controller;

import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import controller.exceptions.ExceptionRegister;
import model.Admin;
import persistence.db.exception.InfraException;

public interface AdminManager {
    boolean insert(String name, String email, String username, String password) throws InfraException, ExceptionRegister;
    Admin retrieve(String username, String password) throws ExceptionNotFound, ExceptionPassword, InfraException;
    void generateReport() throws InfraException;
}