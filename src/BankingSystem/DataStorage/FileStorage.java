package BankingSystem.DataStorage;

import BankingSystem.Exceptions.UserAlreadyExistException;
import BankingSystem.Models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public final class FileStorage implements IDataStorage {
    public static final String USERS_FILE_PATH = "data/users.txt";
    public static final String TOKEN_SEPARATOR = ";";

    private static FileStorage instance = null;

    private File usersFile;
    private int usersNumber;

    private FileStorage() throws IOException {
        this.usersFile = new File(FileStorage.USERS_FILE_PATH);
        this.usersNumber = this.countUsers();
    }

    private int countUsers() throws IOException {
        Scanner scanner = new Scanner(this.usersFile);

        int count = 0;
        while (scanner.hasNextLine()) {
            count++;
            scanner.nextLine();
        }

        scanner.close();

        return count;
    }

    public static FileStorage getInstance() throws IOException {
        if (FileStorage.instance == null) {
            FileStorage.instance = new FileStorage();
        }

        return FileStorage.instance;
    }

    @Override
    public User getUser(String username) throws FileNotFoundException {
        Scanner scanner = new Scanner(this.usersFile);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] tokens = line.split(FileStorage.TOKEN_SEPARATOR);
            int currentId = Integer.parseInt(tokens[0]);
            String currentUsername = tokens[1];
            String currentPassword = tokens[2];

            if (currentUsername.equals(username)) {
                return new User(currentId, currentUsername, currentPassword);
            }
        }

        scanner.close();

        return null;
    }

    @Override
    public void saveUser(String username, String password) throws IOException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username shouldn't be empty!");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password shouldn't be empty!");
        }

        if (this.getUser(username) != null) {
            throw new UserAlreadyExistException("User with username " + username + " has already registered! Please try with another username!");
        }

        User newUser = new User(this.usersNumber + 1, username, password);

        FileWriter writer = new FileWriter(this.usersFile, true);

        writer.write(newUser.toString() + "\n");
        this.usersNumber++;

        writer.close();
    }
}
