package application;
import java.util.Scanner;

public class OptionHandler
{
    static Scanner in = new Scanner(System.in);

    public static String readStringInput()
    {
        return in.next();
    }

    public static String readLineInput()
    {
        return in.nextLine();
    }

    public static int readIntegerInput()
    {
        return in.nextInt();
    }

    public static void showMessage(String data)
    {
        System.out.println(data);
    }
}