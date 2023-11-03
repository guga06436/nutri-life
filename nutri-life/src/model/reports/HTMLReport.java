package model.reports;

import views.Application;

public class HTMLReport extends Report
{
    @Override
    public void generateHead()
    {
        Application.showMessage("------- HTML -------");
    }

    @Override
    public void generateBody()
    {
        Application.showMessage("Generating HTML report");
    }

    @Override
    public void generateFoot()
    {
        Application.showMessage("-------------------");
    }
}