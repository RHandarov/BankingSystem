package BankingSystem;

import BankingSystem.DataStorage.FileStorage;
import BankingSystem.DataStorage.IDataStorage;
import BankingSystem.Exceptions.UserExceptions.UserDoesNotExistException;
import BankingSystem.Exceptions.UserExceptions.WrongLoginCredentialsException;
import BankingSystem.Models.User;

import java.io.IOException;

public class Session {
    private IDataStorage dataStorage;
    private User loggedUser;

    public Session() {
        try {
            this.dataStorage = FileStorage.getInstance();
            this.loggedUser = null;
        } catch (IOException ioException) {
            System.err.println("ERROR: " + ioException.getMessage());
        }
    }

    public IDataStorage getDataStorage() {
        return this.dataStorage;
    }

    public User getLoggedUser() {
        return this.loggedUser;
    }

    public void logInUser(String username, String password) {
        try {
            User user = this.dataStorage.getUser(username);

            if (user == null) {
                throw new UserDoesNotExistException("User " + username + " does not exists! Try again!");
            }

            if (user.arePasswordsSame(password)) {
                this.loggedUser = user;
            } else {
                throw new WrongLoginCredentialsException("The password is wrong! Please try again!");
            }
        } catch (IOException ioException) {
            System.err.println("ERROR: " + ioException.getMessage());
        }
    }

    public void logOutUser()  {
        this.loggedUser = null;
    }

    public void close() throws IOException {
        this.dataStorage.update();
    }
}
