package BankingSystem.DataStorage;

import BankingSystem.Models.User;

import java.io.IOException;

public interface IDataStorage {
    User getUser(String username) throws IOException;

    void saveUser(String username, String password) throws IOException;

    void deleteUser(User user);

    void saveAccount(User owner) throws IOException;

    void deleteAccount(User owner, int accountId);

    void update() throws IOException;
}
