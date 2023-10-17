package controller.impl;

import controller.AdminManager;
import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import model.Admin;
import persistence.db.exception.InfraException;
import persistence.impl.AdminPersistence;

public class AdminManagerImpl implements AdminManager {
    private AdminPersistence ap;

    public AdminManagerImpl() throws InfraException {
        ap = new AdminPersistence();
    }

    @Override
    public boolean add(Admin admin) throws InfraException {
        return ap.add(admin);
    }

    @Override
    public Admin retrieve(String username, String password) throws ExceptionNotFound, ExceptionPassword, InfraException {
        //return ap.retrieve(login, password);
        return null;
    }

    @Override
    public void generateReport() throws InfraException {
        System.out.println("Implementar Relatorio");
    }
}