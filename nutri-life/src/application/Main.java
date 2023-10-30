package application;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import views.MainScreenDesktop;

public class Main
{
    public static void main(String[] args)
    {
        MainScreenDesktop application = new MainScreenDesktop();
        PropertyConfigurator.configure("log4j.properties");
        application.run();
    }
}