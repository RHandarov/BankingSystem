package DataStorage.FileStorage;

public class Constants {
    public static final String USERS_FILE_PATH = "data/users.txt";
    public static final String TOKEN_SEPARATOR = ";";

    private Constants() throws InstantiationException {
        throw new InstantiationException("Cannot instantiate Constants class!");
    }
}
