package application;

import org.apache.log4j.BasicConfigurator;

import views.MainScreenDesktop;

public class Main
{
    public static void main(String[] args)
    {
        MainScreenDesktop application = new MainScreenDesktop();
        BasicConfigurator.configure();
        application.run();
    }
}