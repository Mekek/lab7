package commandManager.commands;

import collectionStorageManager.PostgreSQLManager;
import models.Ticket;
import models.handlers.CollectionHandler;
import models.handlers.TicketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse;

import java.util.Objects;
import java.util.Vector;

/**
 * Removes all elements from the collection that have the same event.
 *
 * @author Mekek
 * @since 1.0
 */
public class RemoveAllByEventName implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.removeAllByEvent");
    private CommandStatusResponse response;

    @Override
    public String getName() {
        return "remove_all_by_event";
    }

    @Override
    public String getDescr() {
        return "Removes all elements from the collection that have the same event name.";
    }

    @Override
    public String getArgs() {
        return "eventName";
    }

    @Override
    public void execute(String[] args) {
        CollectionHandler<Vector<Ticket>, Ticket> collectionHandler = TicketHandler.getInstance();
        String eventName = args[1];

        PostgreSQLManager dbManager = new PostgreSQLManager();
        int count = 0;

        for (Ticket current : collectionHandler.getCollection()) {
            if (dbManager.isTicketOwnedByUser(current.getId()) && Objects.equals(eventName, current.getEvent().getName())) {
                if (dbManager.removeTicketById(current.getId())) {
                    count++;
                }
            }
        }

        collectionHandler.getCollection().removeIf(current -> dbManager.isTicketOwnedByUser(current.getId()) && Objects.equals(eventName, current.getEvent().getName()));
        response = CommandStatusResponse.ofString("Removed " + count + " elements");

        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse getResponse() {
        return response;
    }
}
