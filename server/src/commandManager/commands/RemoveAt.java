package commandManager.commands;

import collectionStorageManager.PostgreSQLManager;
import models.Ticket;
import models.handlers.CollectionHandler;
import models.handlers.TicketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse;

import java.util.Vector;

/**
 * Removes element from collection by id.
 *
 * @author Mekek
 * @since 1.0
 */
public class RemoveAt implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.removeAt");
    private CommandStatusResponse response;

    @Override
    public String getName() {
        return "remove_at";
    }

    @Override
    public String getDescr() {
        return "Removes element from collection by index.";
    }

    @Override
    public String getArgs() {
        return "index";
    }

    @Override
    public void execute(String[] args) {
        CollectionHandler<Vector<Ticket>, Ticket> collectionHandler = TicketHandler.getInstance();

        int index = 0;

        PostgreSQLManager dbManager = new PostgreSQLManager();
        int count = 0;

        Integer rightId = null;

        for (Ticket current : collectionHandler.getCollection()) {
            if (dbManager.isTicketOwnedByUser(current.getId()) && index == Integer.parseInt(args[1])) {
                if (dbManager.removeTicketById(current.getId())) {
                    count++;
                    rightId = current.getId();
                }
            }
            index ++;
        }

        Integer finalRightId = rightId;
        collectionHandler.getCollection().removeIf(current -> dbManager.isTicketOwnedByUser(current.getId()) && finalRightId < current.getId());
        response = CommandStatusResponse.ofString("Removed " + count + " element(s)");

        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse getResponse() {
        return response;
    }
}
