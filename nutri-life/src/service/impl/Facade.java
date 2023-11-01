package service.impl;

import java.util.List;

import controller.AdminManager;
import controller.MealPlanManager;
import controller.NutritionistManager;
import controller.PatientManager;
import controller.impl.AdminManagerImpl;
import controller.impl.MealPlanManagerImpl;
import controller.impl.NutritionistManagerImpl;
import controller.impl.PatientManagerImpl;
import model.reports.Report;
import model.reports.IReportable;
import persistence.db.exception.InfraException;
import service.Command;
import service.LogService;
import service.command.GenerateReportCommand;
import service.command.ListAllCommand;

// Singleton Facade que usa Command
public class Facade
{
    private static Facade instance = null;

    private final AdminManager adminManager;
    private final MealPlanManager mealPlanManager;
    private final NutritionistManager nutritionistManager;
    private final PatientManager patientManager;
    private static final LogService log = LogAdapter.getInstance();

    private Facade() throws InfraException
    {
        adminManager = new AdminManagerImpl();
        mealPlanManager = new MealPlanManagerImpl();
        nutritionistManager = new NutritionistManagerImpl();
        patientManager = new PatientManagerImpl();
    }

    public static synchronized Facade getInstance() throws InfraException
    {
        if (instance == null)
        {
            instance = new Facade();
        }
        return instance;
    }

    public void listAll() throws InfraException
    {
        try
        {
            Command listAllCommand = new ListAllCommand(patientManager, nutritionistManager);
            listAllCommand.execute();
        }
        catch (InfraException e)
        {
            log.logException(e);
            throw e;
        }
    }

    public void generateReport(Report reportGenerator, List<IReportable> reports) throws InfraException
    {
        try
        {
            Command generateReportCommand = new GenerateReportCommand(reportGenerator, reports);
            generateReportCommand.execute();
        }
        catch (InfraException e)
        {
            log.logException(e);
            throw e;
        }
    }
}