package model;

import java.util.List;

public abstract class Report
{
    public abstract void generateReport(List<IReportable> reports);
}