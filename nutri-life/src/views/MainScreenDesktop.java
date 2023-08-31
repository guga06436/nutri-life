package views;
import application.OptionHandler;
import views.NutritionistFormView;
import views.PatientFormView;

public class MainScreenDesktop
{
    public MainScreenDesktop() {}

    public void run()
    {
        NutritionistFormView nutritionistView = new NutritionistFormView();
        PatientFormView patientView = new PatientFormView();

        while(true)
        {
            showMenu();

            switch(OptionHandler.readIntegerInput())
            {
                case 1:

                    break;
                case 2:
                    break;
                case 3:
                    OptionHandler.onExitProgram();
                    System.exit(0);
                    return;
                default:
                    OptionHandler.showMessage("Invalid Option");
                    continue;
            }
        }
    }

    void showMenu()
    {
        OptionHandler.showMessage("""
                        Welcome to NutriLife!
                        Choose the desired option:
                        1-Create Nutritionist
                        2-Create Patient
                        3-End Program
                        Your option: """
                    );
    }
}