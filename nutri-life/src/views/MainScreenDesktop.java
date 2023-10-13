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
                    break;
                case 2:
                    patientView.run();
                    break;
                case 3:
                    OptionHandler.onExitProgram();
                    System.exit(0);
                    break;
                default:
                    OptionHandler.showMessage("Invalid Option");
                    break;
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