package controller;

import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import model.Admin;
import persistence.db.exception.InfraException;

public interface AdminManager {
    boolean add(Admin n) throws InfraException;
    Admin retrieve(String login, String password) throws ExceptionNotFound, ExceptionPassword;
    void generateReport() throws InfraException;
}