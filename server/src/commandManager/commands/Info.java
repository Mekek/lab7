package commandManager.commands;

import models.Ticket;
import models.handlers.TicketHandler;
import models.handlers.CollectionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse;

import java.util.Vector;

/**
 * Shows information about the collection.
 *
 * @author Mekek
 * @since 1.0
 */
public class Info implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.info");
    private CommandStatusResponse response;

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescr() {
        return "Shows information about the collection.";
    }

    @Override
    public void execute(String[] args) {
        CollectionHandler<Vector<Ticket>, Ticket> collectionHandler = TicketHandler.getInstance();

        Vector<Ticket> collection = collectionHandler.getCollection();

        String output = String.format("Now you are operating with collection of type %s, filled with elements of type %s\nCollection size: %d\nInit date: %s",
                collection.getClass().getName(),
                collectionHandler.getFirstOrNew().getClass().getName(),
                collection.size(),
                collectionHandler.getInitDate());

        response = CommandStatusResponse.ofString(output);
        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse getResponse() {
        return response;
    }
}
