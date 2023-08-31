package exceptions;

public class ExceptionPassword extends Exception {
    /* Caso o usuário não esteja cadastrado*/
    public ExceptionPassword(String message) {
        super(message);
    }
}