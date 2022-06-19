package DataStorage.FileStorage;

import DataStorage.IDataStorage;
import DataStorage.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class FileStorage implements IDataStorage {
    private static FileStorage instance = null;

    private FileStorage() {

    }

    public static FileStorage getInstance() {
        if (FileStorage.instance == null) {
            FileStorage.instance = new FileStorage();
        }

        return FileStorage.instance;
    }

    @Override
    public User getUser(String username) throws FileNotFoundException {
        File usersFile = new File(Constants.USERS_FILE_PATH);
        Scanner scanner = new Scanner(usersFile);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] tokens = line.split(Constants.TOKEN_SEPARATOR);
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
}
