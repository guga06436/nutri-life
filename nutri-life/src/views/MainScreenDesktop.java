package views;
import handlers.OptionHandler;
import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import controller.exceptions.ExceptionRegister;

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
                    try {
                        nutritionistView.run();
                    } catch (ExceptionRegister e) {
                        e.printStackTrace();
                    } catch (ExceptionPassword e) {
                        e.printStackTrace();
                    } catch (ExceptionNotFound e) {
                        e.printStackTrace();
                    }
                case 2:
                    try
                    {
                        patientView.run();
                    }
                    catch(ExceptionRegister err){};
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
                        "1- Nutritionist Page\n" +
                        "2- Patient Page\n" +
                        "3- End Program\n" +
                        "Your option: "
                    );
    }
}