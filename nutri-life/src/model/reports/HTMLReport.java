package model.reports;
import java.util.List;

public class HTMLReport extends Report
{
    @Override
    protected String formatReports(List<IReportable> reports)
    {
        return "Generating HTML report";
    }
}