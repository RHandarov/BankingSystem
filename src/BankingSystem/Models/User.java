package BankingSystem.Models;

import BankingSystem.Exceptions.AccountExceptions.AccountNotFoundException;
import BankingSystem.Models.Accounts.Account;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class User {
    private static final String HASH_SALT = "$_617.@Athsh";

    public static String hashPassword(String password) {
        password = User.HASH_SALT + password + User.HASH_SALT;

        try {
            MessageDigest digester = MessageDigest.getInstance("MD5");
            digester.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] hasedBytes = digester.digest();
            return new String(hasedBytes, StandardCharsets.UTF_8).toUpperCase();
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    private int id;
    private String username;
    private String hashedPassword;
    private Map<Integer, Account> accounts;

    public User(int id, String username, String hashedPassword) {
        this.setId(id);
        this.setUsername(username);
        this.setHashedPassword(hashedPassword);
        this.accounts = new HashMap<>();
    }

    public User(User user) {
        if (user == null) {
            throw new NullPointerException("A valid user should be provided!");
        }

        this.setId(user.id);
        this.setUsername(user.username);
        this.setHashedPassword(user.hashedPassword);
        this.accounts = new HashMap<>(user.accounts);
    }

    private void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID should be a positive integer!");
        }

        this.id = id;
    }

    private void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username shouldn't be empty!");
        }

        this.username = username;
    }

    private void setHashedPassword(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            throw new IllegalArgumentException("Password shouldn't be empty!");
        }

        this.hashedPassword = hashedPassword;
    }

    public void addAccount(Account account) {
        if (account == null) {
            throw new NullPointerException("You should provide a valid account!");
        }

        this.accounts.put(account.getId(), account);
    }

    public void removeAccount(int accountId) {
        if (!this.accounts.containsKey(accountId)) {
            throw new AccountNotFoundException("Account with number " + accountId + " does not exist!");
        }

        this.accounts.remove(accountId);
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public Map<Integer, Account> getAccounts() {
        return new HashMap<>(this.accounts);
    }

    public boolean arePasswordsSame(String checkedPassowrd) {
        String test = User.hashPassword(checkedPassowrd);
        return this.hashedPassword.equals(User.hashPassword(checkedPassowrd));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(this.id);
        builder.append(';');
        builder.append(this.username);
        builder.append(';');
        builder.append(this.hashedPassword);

        return builder.toString();
    }

    @Override
    public boolean equals(Object comparedObject) {
        if (comparedObject == null) {
            return false;
        }

        if (this.getClass() != comparedObject.getClass()) {
            return false;
        }

        User comparedUser = (User) comparedObject;

        return this.username == comparedUser.username;
    }
}
