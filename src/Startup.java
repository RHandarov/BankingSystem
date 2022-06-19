import DataStorage.FileStorage.FileStorage;
import DataStorage.IDataStorage;
import UI.Console.ConsoleEngine;

public class Startup {
    private static IDataStorage dataStorage;

    public static IDataStorage getDataStorage() {
        return Startup.dataStorage;
    }

    public static void main(String[] args) {
        //TODO: add more flexibility in the future
        Startup.dataStorage = FileStorage.getInstance();

        ConsoleEngine.run();
    }
}
