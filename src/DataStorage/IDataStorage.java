package DataStorage;

import java.io.FileNotFoundException;

public interface IDataStorage {
    User getUser(String username) throws FileNotFoundException;
}
