package model.reports;

public abstract class Report
{
    public void generateReport()
    {
        generateHead();
        generateBody();
        generateFoot();
    }

    public abstract void generateHead();
    public abstract void generateBody();
    public abstract void generateFoot();
}