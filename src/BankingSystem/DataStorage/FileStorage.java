package BankingSystem.DataStorage;

import BankingSystem.Exceptions.UserExceptions.UserAlreadyExistException;
import BankingSystem.Models.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class FileStorage implements IDataStorage {
    public static final String USERS_FILE_PATH = "data/users.txt";
    public static final String TOKEN_SEPARATOR = ";";

    private static FileStorage instance = null;

    private Map<String, User> users;

    private FileStorage() throws IOException {
        this.loadUsers();
    }

    private void loadUsers() throws IOException {
        this.users = new HashMap<>();

        File usersFile = new File(FileStorage.USERS_FILE_PATH);
        Scanner scanner = new Scanner(usersFile);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] tokens = line.split(FileStorage.TOKEN_SEPARATOR);
            int currentId = Integer.parseInt(tokens[0]);
            String currentUsername = tokens[1];
            String currentPassword = tokens[2];

            this.users.put(currentUsername, new User(currentId, currentUsername, currentPassword));
        }

        scanner.close();
    }

    public static FileStorage getInstance() throws IOException {
        if (FileStorage.instance == null) {
            FileStorage.instance = new FileStorage();
        }

        return FileStorage.instance;
    }

    @Override
    public User getUser(String username) {
        return this.users.get(username);
    }

    @Override
    public void saveUser(String username, String password) throws IOException {
        if (this.getUser(username) != null) {
            throw new UserAlreadyExistException("User with username " + username + " has already registered! Please try with another username!");
        }

        User newUser = new User(this.users.size() + 1, username, User.hashPassword(password));

        FileWriter writer = new FileWriter(FileStorage.USERS_FILE_PATH, true);

        writer.write(newUser + "\n");
        this.users.put(username, newUser);

        writer.close();
    }
}
