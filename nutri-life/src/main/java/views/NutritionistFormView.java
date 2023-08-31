package views;

import entities.Nutritionist;
import exceptions.ExceptionLogin;
import exceptions.ExceptionNotFound;
import exceptions.ExceptionPassword;
import exceptions.ExceptionRegister;
import managers.impl.NutritionistManagerImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class NutritionistFormView {

    /* MELHORAR ARMAZENAMENTO */
    private static List<Nutritionist> nutritionists = new ArrayList<>();

    public NutritionistFormView() { }

    public void menu() {
        System.out.println("-=-=-=-=-=-= NUTRI LIFE =-=-=-=-=-=-");
        System.out.println("[1] - Register");
        System.out.println("[2] - Sign In");
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
        } else if (login.matches(".*\\d.*")) {
            throw new ExceptionRegister("Login must not contain numbers.");
        }

        System.out.print("Password: ");
        String password = sc.next();
        if (password.length() < 8 || password.length() > 20) {
            throw new ExceptionRegister("Password lenght must be between 8 and 20.");
        }

        /* AJEITAR POSTERIORMENTE! */
        Nutritionist nutri = new Nutritionist(name, age, date, crn, login, password);
        nutritionists.add(nutri);
        System.out.println("Registration successful for nutritionist: " + nutri.getName());
    }

    public void signIn(Scanner sc) throws ExceptionPassword, ExceptionNotFound {

        System.out.print("Login: ");
        String login = sc.next();

        System.out.print("Password: ");
        String password = sc.next();

        Nutritionist loggedInNutritionist = findNutritionistByLogin(login);

        if (loggedInNutritionist == null) {
            throw new ExceptionNotFound("Nutritionist not found");
        }

        if (!loggedInNutritionist.getPassword().equals(password)) {
            throw new ExceptionPassword("Invalid password");
        }

        sc.close();

        System.out.println("Login successful for nutritionist: " + loggedInNutritionist.getName());
    }

    private static Nutritionist findNutritionistByLogin(String login) {
        for (Nutritionist nutritionist : nutritionists) {
            if (nutritionist.getLogin().equals(login)) {
                return nutritionist;
            }
        }
        return null;
    }
}
