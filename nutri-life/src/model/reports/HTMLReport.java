package model.reports;
import handlers.OptionHandler;

public class HTMLReport extends Report
{
    @Override
    public void generateReport()
    {
        OptionHandler.showMessage("Generating HTML report");
    }
}