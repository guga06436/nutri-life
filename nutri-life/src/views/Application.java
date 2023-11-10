package views;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import persistence.db.exception.InfraException;
import service.Command;
import service.LogService;
import service.builder.ExitCommandBuilder;
import service.impl.LogAdapter;
import service.status.ApplicationStatus;

public class Application
{
    static Scanner in = new Scanner(System.in);
    static final LogService log = LogAdapter.getInstance();


    public static String readStringInput()
    {
        return in.next();
    }

    public static String readLineInput()
    {
        return in.nextLine();
    }

    public static int readIntegerInput() {
        try {
            int var = in.nextInt();
            return var;
        } catch (InputMismatchException e) {
            System.out.println("You must type an integer! Please try again: ");
            return readIntegerInput();
        }

    }

    public static float readFloatInput() {
        try {
            float var = in.nextFloat();
            return var;
        } catch (InputMismatchException e) {
            System.out.println("You must type a float! Please try again: ");
            return readFloatInput();
        }
    }
    
    public static void showMessage(List<?> data){
    	for(Object object: data) {
    		System.out.println(object);
    	}
    }

    public static void showMessage(String data)
    {
        System.out.println(data);
    }

    public static void showMessage(String data, boolean breakLine)
    {
        if(breakLine)
            System.out.println(data);
        else
            System.out.print(data);
    }

    public static void exitApplication(ApplicationStatus status)
    {
        in.close();
        ExitCommandBuilder exitCommandBuilder = new ExitCommandBuilder();
        exitCommandBuilder.withExitStatus(status);
        Command exitApplicationCommand = exitCommandBuilder.build();

        try {
            exitApplicationCommand.execute();
        } catch (InfraException e) {
            Application.showMessage(e.getMessage());
        }
    }
    public static void logException(Exception e) {  log.logException(e); }
}