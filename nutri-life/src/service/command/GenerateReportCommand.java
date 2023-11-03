package service.command;

import model.reports.Report;
import persistence.db.exception.InfraException;
import service.Command;

public class GenerateReportCommand implements Command {
    private final Report reportGenerator;

    public GenerateReportCommand(Report reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    @Override
    public void execute() throws InfraException {
        if (reportGenerator == null)
            throw new InfraException("Report Generator is Invalid");
        reportGenerator.generateReport();
    }
}