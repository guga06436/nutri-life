package service.command;
import controller.NutritionistManager;
import controller.PatientManager;
import persistence.db.exception.InfraException;

public class ListAllCommand implements Command
{
    private final PatientManager patientManager;
    private final NutritionistManager nutritionistManager;

    public ListAllCommand(PatientManager patientManager, NutritionistManager nutritionistManager)
    {
        this.patientManager = patientManager;
        this.nutritionistManager = nutritionistManager;
    }

    @Override
    public void execute() throws InfraException
    {
        patientManager.listAll();
        nutritionistManager.listAll();
    }
}
