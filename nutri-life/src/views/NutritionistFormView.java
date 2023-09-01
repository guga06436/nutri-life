package views;


import controler.impl.NutritionistManagerImpl;
import model.Nutritionist;
import exceptions.ExceptionNotFound;
import exceptions.ExceptionPassword;
import exceptions.ExceptionRegister;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class NutritionistFormView {

    private NutritionistManagerImpl manager;

    public NutritionistFormView() {
        manager = new NutritionistManagerImpl();
    }

    public void run() throws ExceptionRegister, ExceptionPassword, ExceptionNotFound {

        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("[1] Sign In");
            System.out.println("[2] Register");
            System.out.println("[3] Exit");
            System.out.print("Choose an option: ");
            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    signIn(sc);
                    break;
                case 2:
                    register(sc);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private static Date parseBirthdate(String birthdate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.parse(birthdate);
        } catch (ParseException e) {
            return null;
        }
    }

    public void register(Scanner sc) throws ExceptionRegister {

        System.out.print("Name: ");
        String name = sc.nextLine();
        if (name.isEmpty()) {
            throw new ExceptionRegister("Name must not be empty.");
        }

        System.out.print("Age: ");
        int age = sc.nextInt();
        if (age < 18) {
            throw new ExceptionRegister("Age must be equal or above 18.");
        }

        System.out.print("Birth Date [DD/MM/YYYY]: ");
        String birthdate = sc.next();
        Date date = parseBirthdate(birthdate);
        if (date == null) {
            throw new ExceptionRegister("Invalid Date Format");
        }

        System.out.print("CRN: ");
        String crn = sc.next();

        System.out.print("Login: ");
        String login = sc.next();
        if (login.length() > 12) {
            throw new ExceptionRegister("Login must not be of length above 12.");
        } else if (login.isEmpty()) {
            throw new ExceptionRegister("Login must not be empty.");
        } else if (login.matches(".\\d.")) {
            throw new ExceptionRegister("Login must not contain numbers.");
        }

        System.out.print("Password: ");
        String password = sc.next();
        if (password.length() < 8 || password.length() > 20) {
            throw new ExceptionRegister("Password lenght must be between 8 and 20.");
        }

        Nutritionist nutri = new Nutritionist(name, age, date, crn, login, password);
        boolean registerSuccess = manager.add(nutri);

        if (registerSuccess) {
            System.out.println("Registration successful for nutritionist: " + nutri.getName());
        } else {
            System.out.println("Registration failed for nutritionist: " + nutri.getName());
        }
    }

    public void signIn(Scanner sc) throws ExceptionPassword, ExceptionNotFound {

        System.out.print("Login: ");
        String login = sc.next();

        System.out.print("Password: ");
        String password = sc.next();

        try {
            Nutritionist loggedInNutritionist = manager.retrieve(login, password);
            System.out.println("Login successful for nutritionist: " + loggedInNutritionist.getName());
        } catch (ExceptionNotFound e) {
            System.out.println("Login Failed: " + e.getMessage());
        } catch (ExceptionPassword e) {
            System.out.println("Password Failed: " + e.getMessage());
        }
    }

}