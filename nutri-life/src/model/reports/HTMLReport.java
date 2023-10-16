package model.reports;
import handlers.OptionHandler;

import java.util.List;

public class HTMLReport extends Report
{
    @Override
    public void generateReport(List<IReportable> reports)
    {
        OptionHandler.showMessage("Generating HTML report");
    }
}