package model.reports;
import handlers.OptionHandler;

public class PDFReport extends Report
{
    @Override
    public void generateReport()
    {
        OptionHandler.showMessage("Generating PDF report");
    }
}