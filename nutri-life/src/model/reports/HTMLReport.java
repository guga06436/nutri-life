package model.reports;
import java.util.List;
import views.Application;

public class HTMLReport extends Report
{
    @Override
    public void generateHead(List<IReportable> reports)
    {
        Application.showMessage("------- HTML -------");
    }

    @Override
    public void generateBody(List<IReportable> reports)
    {
        Application.showMessage("Generating HTML report");
    }

    @Override
    public void generateFoot(List<IReportable> reports)
    {
        Application.showMessage("-------------------");
    }
}