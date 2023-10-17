package controller;

import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import model.Admin;
import persistence.db.exception.InfraException;

public interface AdminManager {
    boolean insert(Admin n) throws InfraException;
    Admin retrieve(String username, String password) throws ExceptionNotFound, ExceptionPassword, InfraException;
    void generateReport() throws InfraException;
}