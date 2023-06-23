package commandManager.commands;

import models.Ticket;
import models.handlers.TicketHandler_;
import models.handlers.CollectionHandler_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse_;

import java.util.Vector;

/**
 * Shows information about the collection.
 *
 * @author Mekek
 * @since 1.0
 */
public class Info implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.info");
    private CommandStatusResponse_ response;

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
        CollectionHandler_<Vector<Ticket>, Ticket> collectionHandler = TicketHandler_.getInstance();

        Vector<Ticket> collection = collectionHandler.getCollection();

        String sb = "Now you are operating with collection of type " + collection.getClass().getName() + ", filled with elements of type " + collectionHandler.getFirstOrNew().getClass().getName() + '\n' +
                "Collection size: " + collection.size() + '\n' +
                "Init date: " + collectionHandler.getInitDate();

        response = CommandStatusResponse_.ofString(sb);
        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse_ getResponse() {
        return response;
    }
}
