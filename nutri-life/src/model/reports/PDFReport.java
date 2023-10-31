package model.reports;
import java.util.List;
import service.Application;

public class PDFReport extends Report
{
    @Override
    public void generateHead(List<IReportable> reports)
    {
        Application.showMessage("------- PDF -------");
    }

    @Override
    public void generateBody(List<IReportable> reports)
    {
        Application.showMessage("Generating PDF report");
    }

    @Override
    public void generateFoot(List<IReportable> reports)
    {
        Application.showMessage("-------------------");
    }
}