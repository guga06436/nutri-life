package views;
import handlers.OptionHandler;
import service.viewobserver.IViewObserver;

public class MainScreenDesktop implements IViewObserver, AutoCloseable
{
    NutritionistFormView nutritionistView;
    PatientFormView patientView;
    AdminFormView adminView;

    public MainScreenDesktop()
    {
        nutritionistView = new NutritionistFormView();
        patientView = new PatientFormView();
        adminView = new AdminFormView();

        subscribeToSubjects();
    }

    public void run()
    {
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
                    adminView.run();
                case 4:
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
                        "3- Admin Page\n" +
                        "3- End Program\n" +
                        "Your option: "
                    );
    }

    // Poderia modificar o uso, mas para simplificar apenas printa o historico de navegacao
    @Override
    public void onActionCalled(ViewAction action)
    {
        OptionHandler.showMessage("[" +
                                        action.getActor().getClass().getName() +
                                        "]: " +
                                        action.getMessage());

    }

    @Override
    public void close()
    {
        unsubscribeFromSubjects();
    }

    private void subscribeToSubjects()
    {
        if(nutritionistView != null)
            nutritionistView.addObserver(this);

        if(patientView != null)
            patientView.addObserver(this);

        if(adminView != null)
            adminView.addObserver(this);
    }

    private void unsubscribeFromSubjects()
    {
        if(nutritionistView != null)
            nutritionistView.removeObserver(this);

        if(patientView != null)
            patientView.removeObserver(this);

        if(adminView != null)
            adminView.removeObserver(this);
    }
}