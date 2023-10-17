package model.reports;
import handlers.OptionHandler;

import java.util.List;

public class PDFReport extends Report
{
    @Override
    public void generateReport(List<IReportable> reports)
    {
        OptionHandler.showMessage("Generating PDF report");
    }
}