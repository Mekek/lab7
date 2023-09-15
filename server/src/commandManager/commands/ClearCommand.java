package commandManager.commands;

import collectionStorageManager.PostgreSQLManager;
import models.Ticket;
import models.handlers.CollectionHandler;
import models.handlers.TicketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse;

import java.util.List;
import java.util.Vector;

/**
 * Clears collection
 *
 * @author Mekek
 * @since 1.0
 */
public class ClearCommand implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.clear");
    private CommandStatusResponse response;

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
        TicketHandler ticketHandler = TicketHandler.getInstance();
        PostgreSQLManager postgreSQLManager = new PostgreSQLManager();

        // Clear the collection in the database for the specified user and get the list of deleted ticket IDs
        List<Integer> deletedTicketIds = postgreSQLManager.clearTicketsForUser();

        if (!deletedTicketIds.isEmpty()) {
            // Remove tickets with the deleted ticket IDs from the in-memory collection
            ticketHandler.getCollection().removeIf(ticket -> deletedTicketIds.contains(ticket.getId()));
            response = CommandStatusResponse.ofString("Cleared!");
        } else {
            response = CommandStatusResponse.ofString("Failed to clear the collection.");
        }

        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse getResponse() {
        return response;
    }
}
