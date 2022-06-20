package BankingSystem.Exceptions.UserExceptions;

public class WrongLoginCredentialsException extends RuntimeException {
    public WrongLoginCredentialsException(String message) {
        super(message);
    }
}
