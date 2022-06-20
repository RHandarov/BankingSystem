package BankingSystem.Models;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private static final String HASH_SALT = "$_617.@Athsh";

    private static String hashPassword(String password) {
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

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.hashedPassword = User.hashPassword(User.HASH_SALT + password + User.HASH_SALT);
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
