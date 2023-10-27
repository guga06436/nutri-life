package handlers;
import java.util.InputMismatchException;
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

    public static void onExitProgram()
    {
        in.close();
    }
}