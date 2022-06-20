import DataStorage.FileStorage;
import DataStorage.IDataStorage;
import Models.User;
import UI.Console.ConsoleEngine;

import java.io.IOException;

public class Startup {
    private static IDataStorage dataStorage;
    private static User loggedUser;

    public static IDataStorage getDataStorage() {
        return Startup.dataStorage;
    }

    public static User getLoggedUser() {
        return Startup.loggedUser;
    }

    public static void main(String[] args) throws IOException {
        //TODO: add more flexibility in the future
        Startup.dataStorage = FileStorage.getInstance();
        Startup.loggedUser = null;

        ConsoleEngine.run();
    }
}
