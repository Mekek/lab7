package commandManager.commands;

import collectionStorageManager.PostgreSQLManager;
import models.Ticket;
import models.handlers.TicketHandler;
import models.handlers.CollectionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse;

import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Removes element from collection by id.
 *
 * @author Mekek
 * @since 1.0
 */
public class RemoveById implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.removeByID");
    private CommandStatusResponse response;

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String getDescr() {
        return "Removes element from collection by id.";
    }

    @Override
    public String getArgs() {
        return "id";
    }

    @Override
    public void execute(String[] args) {
        CollectionHandler<Vector<Ticket>, Ticket> collectionHandler = TicketHandler.getInstance();
        int ticketId = Integer.parseInt(args[1]);

        PostgreSQLManager dbManager = new PostgreSQLManager();

        if (!dbManager.isTicketOwnedByUser(ticketId)) {
            boolean removed = dbManager.removeTicketById(ticketId);
            if (removed) {
                collectionHandler.getCollection().removeIf(ticket -> Objects.equals(ticket.getId(), ticketId));
                response = CommandStatusResponse.ofString("Element removed.");
            } else {
                response = CommandStatusResponse.ofString("Failed to remove the element.");
            }
        } else {
            response = CommandStatusResponse.ofString("Element with that id doesn't exist or you don't have permission to remove it.");
        }

        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse getResponse() {
        return response;
    }
}
