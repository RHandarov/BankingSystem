package BankingSystem.Exceptions.AccountExceptions;

public class InvalidAccountOwnerException extends RuntimeException {
    public InvalidAccountOwnerException(String message) {
        super(message);
    }
}
