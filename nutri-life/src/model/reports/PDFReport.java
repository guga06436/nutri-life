package model.reports;

import views.Application;

public class PDFReport extends Report
{
    @Override
    public void generateHead()
    {
        Application.showMessage("------- PDF -------");
    }

    @Override
    public void generateBody()
    {
        Application.showMessage("Generating PDF report");
    }

    @Override
    public void generateFoot()
    {
        Application.showMessage("-------------------");
    }
}