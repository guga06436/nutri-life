package model;

import java.util.List;

import handlers.OptionHandler;
import model.reports.IReportable;

public class HTMLReport extends Report
{
    @Override
    public void generateReport(List<IReportable> reports)
    {
        OptionHandler.showMessage("Generating HTML report");
    }
}