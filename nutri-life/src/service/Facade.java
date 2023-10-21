package service;

import java.util.List;

import controller.AdminManager;
import controller.MealPlanManager;
import controller.NutritionistManager;
import controller.PatientManager;
import controller.impl.AdminManagerImpl;
import controller.impl.MealPlanManagerImpl;
import controller.impl.NutritionistManagerImpl;
import controller.impl.PatientManagerImpl;
import model.Report;
import model.reports.IReportable;
import persistence.db.exception.InfraException;

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
            //mealPlanManager.listAll();
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