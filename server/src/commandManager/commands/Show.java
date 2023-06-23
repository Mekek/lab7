package commandManager.commands;

import models.Ticket;
import models.handlers.CollectionHandler_;
import models.handlers.TicketHandler_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse_;

import java.util.Vector;

/**
 * Shows every element of the collection in toString() interpretation.
 *
 * @author Mekek
 * @since 1.0
 */
public class Show implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.show");
    private CommandStatusResponse_ response;

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescr() {
        return "Shows every element of the collection in toString() interpretation.";
    }

    @Override
    public void execute(String[] args) {
        CollectionHandler_<Vector<Ticket>, Ticket> collectionHandler = TicketHandler_.getInstance();

        StringBuilder sb = new StringBuilder();

        collectionHandler.getCollection().forEach(e -> sb.append(e.toString()).append('\n'));
        response = CommandStatusResponse_.ofString(sb.toString());

        if (collectionHandler.getCollection().isEmpty()) {
            response = CommandStatusResponse_.ofString("There's nothing to show.");
        }

        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse_ getResponse() {
        return response;
    }
}
