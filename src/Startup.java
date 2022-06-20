import DataStorage.FileStorage;
import DataStorage.IDataStorage;
import Models.User;
import UI.Console.ConsoleEngine;

public class Startup {
    private static IDataStorage dataStorage;
    private static User loggedUser;

    public static IDataStorage getDataStorage() {
        return Startup.dataStorage;
    }

    public static User getLoggedUser() {
        return Startup.loggedUser;
    }

    public static void main(String[] args) {
        //TODO: add more flexibility in the future
        Startup.dataStorage = FileStorage.getInstance();
        Startup.loggedUser = null;

        ConsoleEngine.run();
    }
}
