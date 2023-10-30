package service;
import persistence.db.exception.InfraException;
import service.builder.ExitCommandBuilder;
import service.command.ExitApplicationCommand;
import service.impl.LogAdapter;
import service.status.ApplicationStatus;

import java.util.InputMismatchException;
import java.util.Scanner;

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
            return in.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("You must type an integer! Please try again: ");
            return readIntegerInput();
        }

    }

    public static float readFloatInput() {
        try {
            return in.nextFloat();
        } catch (InputMismatchException e) {
            System.out.println("You must type a float! Please try again: ");
            return readFloatInput();
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
            throw new RuntimeException(e);
        }
    }
    public static void logException(Exception e) {  log.logException(e); }
    public static void logDebug(String message) {
        log.logDebug(message);
    }
}