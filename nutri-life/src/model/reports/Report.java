package model.reports;

import java.util.List;

import model.reports.IReportable;

public abstract class Report
{
    public void generateReport(List<IReportable> reports)
    {
        generateHead(reports);
        generateBody(reports);
        generateFoot(reports);
    }

    public abstract void generateHead(List<IReportable> reports);
    public abstract void generateBody(List<IReportable> reports);
    public abstract void generateFoot(List<IReportable> reports);
}