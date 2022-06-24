package BankingSystem.Exceptions.AccountExceptions;

public class InvalidAccountRemovalException extends RuntimeException {
    public InvalidAccountRemovalException(String message) {
        super(message);
    }
}
