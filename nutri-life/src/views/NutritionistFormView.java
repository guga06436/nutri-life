package views;


import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controler.impl.NutritionistManagerImpl;
import exceptions.ExceptionNotFound;
import exceptions.ExceptionPassword;
import exceptions.ExceptionRegister;
import model.Nutritionist;

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

        System.out.print("CRN: ");
        String crn = sc.next();

        System.out.print("Username: ");
        String username = sc.next();
        if (username.length() > 12) {
            throw new ExceptionRegister("Login must not be of length above 12.");
        } else if (username.isEmpty()) {
            throw new ExceptionRegister("Login must not be empty.");
        } else if (username.matches(".\\d.")) {
            throw new ExceptionRegister("Login must not contain numbers.");
        }

        System.out.print("Password: ");
        String password = sc.next();
        if (password.length() < 8 || password.length() > 20) {
            throw new ExceptionRegister("Password lenght must be between 8 and 20.");
        }
        
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(password);
        
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        
        if(count < 2) {
        	throw new ExceptionRegister("The password must have at least 2 numbers");
        }
        
        boolean containsLetters = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                containsLetters = true;
                break; // Se encontrar uma letra, sai do loop
            }
        }

        if (!containsLetters) {
            throw new ExceptionRegister("The password msut have at least 1 letter");
        } 

        Nutritionist nutri = new Nutritionist(name, age, crn, username, password);
        boolean registerSuccess = manager.add(nutri);

        if (registerSuccess) {
            System.out.println("Registration successful for nutritionist: " + nutri.getName());
        } else {
            System.out.println("Registration failed for nutritionist: " + nutri.getName());
        }
    }

    public void signIn(Scanner sc) throws ExceptionPassword, ExceptionNotFound {

        System.out.print("Username: ");
        String username = sc.next();

        System.out.print("Password: ");
        String password = sc.next();

        try {
            Nutritionist loggedInNutritionist = manager.retrieve(username, password);
            System.out.println("Login successful for nutritionist: " + loggedInNutritionist.getName());
        } catch (ExceptionNotFound e) {
            System.out.println("Login Failed: " + e.getMessage());
        } catch (ExceptionPassword e) {
            System.out.println("Password Failed: " + e.getMessage());
        }
    }

}