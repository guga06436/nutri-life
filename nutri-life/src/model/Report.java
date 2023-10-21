package model;

import java.util.List;

import model.reports.IReportable;

public abstract class Report
{
    public abstract void generateReport(List<IReportable> reports);
}