package model.reports;
import java.util.List;

public class PDFReport extends Report
{
    @Override
    protected String formatReports(List<IReportable> reports)
    {
        return "Generating PDF report";
    }
}