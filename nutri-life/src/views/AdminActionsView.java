package views;

import model.Admin;
import model.reports.HTMLReport;
import model.reports.PDFReport;
import persistence.db.exception.InfraException;
import service.impl.Facade;
import service.status.ErrorApplicationStatus;
import service.viewobserver.ViewSubject;

public class AdminActionsView extends ViewSubject
{
    private Facade manager;
    private final Admin loggedInAdmin;

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
            Application.showMessage("Choose an option: ");
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
        try {
            manager.listAll();
        } catch (InfraException e) {
            Application.showMessage(e.getMessage());
        }
    }

    private void generateReport() {
        boolean running = true;
        while (running) {
            Application.showMessage("Choose report format:");
            Application.showMessage("[1] PDF");
            Application.showMessage("[2] HTML");
            Application.showMessage("[3] Exit");

            int option = Application.readIntegerInput();

            switch (option) {
                case (1):
                    try {
                        manager.generateReport(new PDFReport());
                    } catch (InfraException e) {
                        Application.showMessage(e.getMessage());
                    }
                    break;
                case (2):
                    try {
                        manager.generateReport(new HTMLReport());
                    } catch (InfraException e) {
                        Application.showMessage(e.getMessage());
                    }
                    break;
                case (3):
                    Application.showMessage("Exiting...");
                    running = false;
                    break;
                default:
                    Application.showMessage("Invalid Option");
            }
        }

    }
}
