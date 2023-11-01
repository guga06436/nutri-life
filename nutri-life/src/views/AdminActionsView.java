package views;

import model.Admin;
import persistence.db.exception.InfraException;
import service.impl.Facade;
import service.status.ErrorApplicationStatus;
import service.viewobserver.ViewSubject;

public class AdminActionsView extends ViewSubject
{
    private Facade manager;
    private Admin loggedInAdmin;

    public AdminActionsView(Admin loggedInAdmin) {
        try {
            this.manager = Facade.getInstance();
        } catch (InfraException e) {
            Application.showMessage("Jeez! We noticed an error with our infrastructure. Please try again later.");
            Application.exitApplication(new ErrorApplicationStatus());
        }

        this.loggedInAdmin = loggedInAdmin;
    }

    public void run()
    {
        notifyObservers("called run()");

        boolean running = true;
        while (running) {
            Application.showMessage("Welcome, " + loggedInAdmin.getName() + "!");
            Application.showMessage("[1] List all");
            Application.showMessage("[2] Generate report");
            Application.showMessage("[3] Log out");
            Application.showMessage("Escolha uma opção: ");
            int option = Application.readIntegerInput();
            Application.readLineInput();

            switch (option) {
                case 1:
                    notifyObservers("called listAll()");
                    listAll();
                    break;
                case 2:
                    notifyObservers("called generateReport()");
                    generateReport();
                    break;
                case 3:
                    notifyObservers("exiting view");
                    Application.showMessage("Exiting...");
                    running = false;
                    break;
                default:
                    Application.showMessage("Invalid Option");
                    break;
            }
        }
    }

    private void listAll() {
        // Implemente a lógica para listar todos os itens desejados.
        Application.showMessage("Listar todos - Ação a ser implementada");
        //manager.listAll();
    }

    private void generateReport() {
        // Implemente a lógica para gerar um relatório.
        Application.showMessage("Gerar relatório - Ação a ser implementada");
        //manager.generateReport();
    }
}
