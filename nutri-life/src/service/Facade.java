package service;

import model.*;
import controller.*;
import controller.impl.*;
import controller.exceptions.*;
import model.reports.IReportable;
import model.reports.Report;
import persistence.db.exception.InfraException;

import java.util.List;

public class Facade
{
    private static Facade instance = null;

    private final PatientManager patientManager;
    private final MealPlanManager mealPlanManager;
    //private final FoodManager foodManager;
    private final NutritionistManager nutritionistManager;
    private final AdminManager adminManager;
    //private final RecipeManager recipeManager;

    private Facade() throws InfraException
    {
        patientManager = new PatientManagerImpl();
        mealPlanManager = new MealPlanManagerImpl();
        //foodManager = new FoodManagerImpl();
        nutritionistManager = new NutritionistManagerImpl();
        adminManager = new AdminManagerImpl();
    }

    public static synchronized Facade getInstance() throws InfraException
    {
        if (instance == null)
        {
            instance = new Facade();
        }
        return instance;
    }

    public void listAll()
    {
        try
        {
            patientManager.listAll();
            nutritionistManager.listAll();
            mealPlanManager.listAll();
            //foodManager.listAll();
            //recipeManager.listAll();
        }
        catch (InfraException e)
        {
            e.printStackTrace();
        }
    }

    public void generateReport(Report reportGenerator, List<IReportable> reports)
    {
        reportGenerator.generateReport(reports);
    }
}