package model;

import java.util.List;

import service.Application;
import model.reports.IReportable;

public class HTMLReport extends Report
{
    @Override
    public void generateReport(List<IReportable> reports)
    {
        Application.showMessage("Generating HTML report");
    }
}