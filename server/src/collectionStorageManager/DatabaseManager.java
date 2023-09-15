package collectionStorageManager;

import models.Ticket;

import java.util.ArrayList;

public interface DatabaseManager {
    ArrayList<Ticket> getCollectionFromDatabase();

    void writeCollectionToDatabase();
}
