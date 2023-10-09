package service;

import views.*;
import model.*;
import controller.*;
import controller.impl.*;
import controller.exceptions.*;
import persistence.db.exception.InfraException;

public class Facade 
{
    private static Facade instance = null;
    
    private NutritionistFormView nutritionistFormView;
    private PatientFormView patientFormView;

    private NutritionistManager nutritionistManager;
    private PatientManager patientManager;

    public Facade() throws InfraException 
    {
        nutritionistFormView = new NutritionistFormView();
        patientFormView = new PatientFormView();

        nutritionistManager = new NutritionistManagerImpl();
        patientManager = new PatientManagerImpl();
    }

    public static Facade getInstance() throws InfraException
    {
        if (instance == null)
        {
            instance = new Facade();
        }
        return instance;
    }

    public boolean addNutritionist(Nutritionist n) 
    {
        try 
        {
            return nutritionistManager.add(n);
        } 
        catch (InfraException e) 
        {
            // Handle exceptions if necessary
            e.printStackTrace();
            return false;
        }
    }

    public Nutritionist retrieveNutritionist(String login, String password) 
    {
        try 
        {
            return nutritionistManager.retrieve(login, password);
        }
        catch (ExceptionNotFound | ExceptionPassword e) 
        {
            // Handle exceptions if necessary
            e.printStackTrace();
            return null;
        }
    }

    public boolean addPatient(Patient p) 
    {
        try 
        {
            return patientManager.add(p);
        }
        catch (InfraException e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    public void listAllPatients() 
    {
        try
        {
            patientManager.listAll();
        }
        catch (InfraException e) 
        {
            e.printStackTrace();
        }
    }

    public void runNutritionistFormView() 
    {
        try 
        {
            nutritionistFormView.run();
        } 
        catch (ExceptionRegister | ExceptionPassword | ExceptionNotFound e) 
        {
            // Handle exceptions from the view if needed
            e.printStackTrace();
        }
    }

    public void runPatientFormView() 
    {
        try 
        {
            patientFormView.run();
        }
        catch (ExceptionRegister e) 
        {
            // Handle exceptions from the view if needed
            e.printStackTrace();
        }
    }

    public void viewPatientFormView() 
    {
        try 
        {
            patientFormView.view();
        }
        catch (ExceptionRegister e) 
        {
            // Handle exceptions from the view if needed
            e.printStackTrace();
        }
    }
}
