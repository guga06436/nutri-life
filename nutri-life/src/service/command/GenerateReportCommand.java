package service.command;

import model.Report;
import model.reports.IReportable;
import persistence.db.exception.InfraException;
import java.util.List;

public class GenerateReportCommand implements Command
{
    private final Report reportGenerator;
    private final List<IReportable> reports;

    public GenerateReportCommand(Report reportGenerator, List<IReportable> reports)
    {
        this.reportGenerator = reportGenerator;
        this.reports = reports;
    }

    @Override
    public void execute() throws InfraException
    {
        if(reportGenerator == null)
            throw new InfraException("Report Generator is Invalid");
        if(reports == null)
            throw new InfraException("Reports are Invalid");
        reportGenerator.generateReport(reports);
    }
}