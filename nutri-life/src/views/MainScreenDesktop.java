package views;
import application.OptionHandler;
import exceptions.ExceptionRegister;
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
                    nutritionistView.menu();
                    break;
                case 2:
                    try
                    {
                        patientView.view();
                    }
                    catch(ExceptionRegister err){};
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
        OptionHandler.showMessage(
                        "Welcome to NutriLife!\n" +
                        "Choose the desired option:\n" +
                        "1-Create Nutritionist\n" +
                        "2-Create Patient\n" +
                        "3-End Program\n" +
                        "Your option: "
                    );
    }
}