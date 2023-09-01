package views;


import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controler.PatientManager;
import controler.impl.PatientManagerImpl;
import exceptions.ExceptionLogin;
import exceptions.ExceptionNotFound;
import exceptions.ExceptionPassword;
import exceptions.ExceptionRegister;
import model.Patient;

public class PatientFormView {

    private static PatientManager patientManager;

    /*lida com registro dos pacientes*/
    public PatientFormView(){
        patientManager = new PatientManagerImpl();
    }

    public void run() throws ExceptionRegister {

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
                    try {
                        signIn(sc);
                    } catch (ExceptionLogin | ExceptionPassword e) {
                        System.out.println("Login failed: " + e.getMessage());
                    } catch (ExceptionNotFound e){
                        System.out.println("Login failed: Patient not found");
                    }
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

    public void view() throws ExceptionRegister {

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
                    try {
                        signIn(sc);
                    } catch (ExceptionLogin | ExceptionPassword e) {
                        System.out.println("Login failed: " + e.getMessage());
                    } catch (ExceptionNotFound e){
                        System.out.println("Login failed: Patient not found");
                    }
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

    private static void signIn(Scanner scanner) throws ExceptionLogin, ExceptionPassword, ExceptionNotFound {

        System.out.print("Login: ");
        String login = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Patient loggedInPatient = patientManager.retrieve(login, password);

        System.out.println("Login successful for patient" + loggedInPatient.getName());

    }

    private static void register(Scanner scanner) throws ExceptionRegister {

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Height: ");
        float height = scanner.nextFloat();

        System.out.print("Weight: ");
        float weight = scanner.nextFloat();
        scanner.nextLine();

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

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

        Patient newPatient = new Patient(username, password, name, cpf, age, height, weight);
        boolean registerSuccess = patientManager.add(newPatient);

        /*Condição de Sucesso*/
        if (registerSuccess) {
            System.out.println("Success" + newPatient.getName());
        } else {
            System.out.println("Failed" + newPatient.getName());
        }

        if (username.length() > 12) {
            throw new ExceptionRegister("Login must not be of length above 12.");
        } else if (username.isEmpty()) {
            throw new ExceptionRegister("Login must not be empty.");
        } else if (username.matches(".*\\d.*")) {
            throw new ExceptionRegister("Login must not contain numbers.");
        }
    }

}
