package views;
import application.OptionHandler;

public class MainScreenDesktop
{
    public MainScreenDesktop() {}

    public void showMenu()
    {
        String data = "Welcome to NutriLife!\n";
        data += "Choose the desired option:\n";
        data += "1-Create Nutritionist\n";
        data += "2-Create Patient\n";
        data += "3-End Program\n";
        data += "Your option: ";

        OptionHandler.showInputDialog(data);
    }

    public void readUserInput(String option)
    {
        
    }
}