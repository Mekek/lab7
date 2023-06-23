package commandManager.commands;

import models.Ticket;
import models.handlers.TicketHandler_;
import models.handlers.CollectionHandler_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse_;

import java.util.Vector;

/**
 * Clears collection
 *
 * @author Mekek
 * @since 1.0
 */
public class ClearCommand_ implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.clear");
    private CommandStatusResponse_ response;

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescr() {
        return "Clears collection";
    }

    @Override
    public void execute(String[] args) {
        CollectionHandler_<Vector<Ticket>, Ticket> collectionHandler = TicketHandler_.getInstance();

        collectionHandler.clearCollection();

        response = CommandStatusResponse_.ofString("Cleared!");
        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse_ getResponse() {
        return response;
    }
}
