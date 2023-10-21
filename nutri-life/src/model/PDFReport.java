package model;

import java.util.List;

import handlers.OptionHandler;
import model.reports.IReportable;

public class PDFReport extends Report
{
    @Override
    public void generateReport(List<IReportable> reports)
    {
        OptionHandler.showMessage("Generating PDF report");
    }
}