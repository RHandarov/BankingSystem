package BankingSystem.Models;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public User(int id, String username, String hashedPassword) {
        this.setId(id);
        this.setUsername(username);
        this.setHashedPassword(hashedPassword);
    }

    private void setId(int id) {
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

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
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
}
