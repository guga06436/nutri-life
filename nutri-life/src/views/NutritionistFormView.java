package views;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.exceptions.ExceptionNotFound;
import controller.exceptions.ExceptionPassword;
import controller.exceptions.ExceptionRegister;
import handlers.OptionHandler;
import model.Nutritionist;
import persistence.db.exception.InfraException;
import service.Facade;

public class NutritionistFormView {

    private Facade manager;

    public NutritionistFormView() {
        try {
			manager = new Facade();
		} catch (InfraException e) {
			System.out.println("Jeez! We noticed an error with our infrastructure. Please try again later."); // Melhorar tratamento
		}
    }

    public void run() throws ExceptionRegister, ExceptionPassword, ExceptionNotFound {

        OptionHandler sc = new OptionHandler();

        while(true){
            System.out.println("[1] Sign In");
            System.out.println("[2] Register");
            System.out.println("[3] Exit");
            System.out.print("Choose an option: ");
            int option = sc.readIntegerInput();
            sc.readLineInput();

            switch (option) {
                case 1:
                    signIn(sc);
                case 2:
                    register(sc);
                case 3:
                    System.out.println("Exiting...");
                    sc.onExitProgram();
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public void register(OptionHandler sc) throws ExceptionRegister {

        System.out.print("Name: ");
        String name = sc.readLineInput();

        System.out.print("Age: ");
        int age = sc.readIntegerInput();
        if (age < 18) {
            throw new ExceptionRegister("Age must be equal or above 18.");
        }

        System.out.print("CRN: ");
        String crn = sc.readStringInput();

        System.out.print("Username: ");
        String username = sc.readStringInput();
        if (username.length() > 12) {
            throw new ExceptionRegister("Login must not be of length above 12.");
        } else if (username.isEmpty()) {
            throw new ExceptionRegister("Login must not be empty.");
        } else if (username.matches(".\\d.")) {
            throw new ExceptionRegister("Login must not contain numbers.");
        }

        System.out.print("Password: ");
        String password = sc.readStringInput();
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
        boolean registerSuccess = false;
		try {
			registerSuccess = manager.addNutritionist(nutri);
		} catch (InfraException e) {
			System.out.println(e.getMessage()); // Melhorar Tratamento
		}

        if (registerSuccess) {
            System.out.println("Registration successful for nutritionist: " + nutri.getName());
        } else {
            System.out.println("Registration failed for nutritionist: " + nutri.getName());
        }
    }

    public void signIn(OptionHandler sc) throws ExceptionPassword, ExceptionNotFound {

        System.out.print("Username: ");
        String username = sc.readStringInput();

        System.out.print("Password: ");
        String password = sc.readStringInput();

        try {
            Nutritionist loggedInNutritionist = manager.retrieveNutritionist(username, password);
            System.out.println("Login successful for nutritionist: " + loggedInNutritionist.getName());
        } catch (ExceptionNotFound e) {
            System.out.println("Login Failed: " + e.getMessage());
        } catch (ExceptionPassword e) {
            System.out.println("Password Failed: " + e.getMessage());
        }
    }

}