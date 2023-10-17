package views;

import handlers.OptionHandler;
import model.Admin;
import persistence.db.exception.InfraException;
import service.Facade;

public class AdminActionsView {

    private Facade manager;
    private Admin loggedInAdmin;

    public AdminActionsView(Admin loggedInAdmin) {
        try {
            this.manager = Facade.getInstance();
        } catch (InfraException e) {
            System.out.println("Jeez! We noticed an error with our infrastructure. Please try again later.");
            System.exit(1);
        }

        this.loggedInAdmin = loggedInAdmin;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("Welcome, " + loggedInAdmin.getName() + "!");
            System.out.println("[1] List all");
            System.out.println("[2] Generate report");
            System.out.println("[3] Log out");
            System.out.print("Escolha uma opção: ");
            int option = OptionHandler.readIntegerInput();
            OptionHandler.readLineInput();

            switch (option) {
                case 1:
                    listAll();
                    break;
                case 2:
                    generateReport();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid Option");
                    break;
            }
        }
    }

    private void listAll() {
        // Implemente a lógica para listar todos os itens desejados.
        System.out.println("Listar todos - Ação a ser implementada");
        //manager.listAll();
    }

    private void generateReport() {
        // Implemente a lógica para gerar um relatório.
        System.out.println("Gerar relatório - Ação a ser implementada");
        //manager.generateReport();
    }
}
