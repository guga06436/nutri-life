package model.reports;

import handlers.OptionHandler;

import java.util.List;

public abstract class Report
{
    protected abstract String formatReports(List<IReportable> reports);

    public final void generateReport(List<IReportable> reports)
    {
        String message = formatReports(reports);
        OptionHandler.showMessage(message);
    }
}