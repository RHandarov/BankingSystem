package BankingSystem.Exceptions.AccountExceptions;

public class InvalidTransferOperationException extends RuntimeException {
    public InvalidTransferOperationException(String message) {
        super(message);
    }
}
