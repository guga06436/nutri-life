package views;
import handlers.OptionHandler;

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
                    nutritionistView.run();
                case 2:
                    patientView.run();
                case 3:
                    OptionHandler.onExitProgram();
                    System.exit(0);
                    return;
                default:
                    OptionHandler.showMessage("Invalid Option");
            }
        }
    }

    void showMenu()
    {
        OptionHandler.showMessage(
                        "Welcome to NutriLife!\n" +
                        "Choose the desired option:\n" +
                        "1- Nutritionist Page\n" +
                        "2- Patient Page\n" +
                        "3- End Program\n" +
                        "Your option: "
                    );
    }
}