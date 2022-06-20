package BankingSystem;

import BankingSystem.DataStorage.FileStorage;
import BankingSystem.DataStorage.IDataStorage;
import BankingSystem.Models.User;

import java.io.IOException;

public class Session {
    private IDataStorage dataStorage;
    private User loggedUser;

    public Session() {
        try {
            this.dataStorage = FileStorage.getInstance();
            this.loggedUser = null;
        } catch (IOException exception) {
            System.err.println("ERROR: " + exception.getMessage());
        }
    }

    public IDataStorage getDataStorage() {
        return this.dataStorage;
    }

    public User getLoggedUser() {
        return this.loggedUser;
    }
}
