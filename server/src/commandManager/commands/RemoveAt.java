package commandManager.commands;

import models.Ticket;
import models.handlers.TicketHandler_;
import models.handlers.CollectionHandler_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse_;

import java.util.Vector;

/**
 * Removes element from collection by id.
 *
 * @author Mekek
 * @since 1.0
 */
public class RemoveAt implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.removeAt");
    private CommandStatusResponse_ response;

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
        CollectionHandler_<Vector<Ticket>, Ticket> collectionHandler = TicketHandler_.getInstance();

        int index = 0;
        for (Ticket ticket : collectionHandler.getCollection()) {
            if (index  == Float.valueOf(args[1])) {
                collectionHandler.getCollection().remove(ticket);
                index = -1;
                break;
            }
            index ++;
        }
        if (index == -1) {
            response = CommandStatusResponse_.ofString("Executed.");
        }
        else response = CommandStatusResponse_.ofString("Element with that index doesn't exists.");

        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse_ getResponse() {
        return response;
    }
}
