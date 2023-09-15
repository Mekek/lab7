package commandManager.commands;

import collectionStorageManager.PostgreSQLManager;
import models.Ticket;
import models.handlers.CollectionHandler;
import models.handlers.TicketIDHandler;
import models.handlers.TicketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;

/**
 * Updates element by its ID.
 *
 * @author Mekek
 * @since 1.0
 */
public class Update implements Command, ArgumentConsumer<Ticket> {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.update");
    private CommandStatusResponse response;
    private Ticket obj;

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescr() {
        return "Updates the value of the collection element whose id is equal to the given.";
    }

    @Override
    public String getArgs() {
        return "id {element}";
    }

    @Override
    public void execute(String[] args) {
        CollectionHandler<Vector<Ticket>, Ticket> collectionHandler = TicketHandler.getInstance();

        int finalId = Integer.parseInt(args[1]);

        PostgreSQLManager dbManager = new PostgreSQLManager();

        if (!dbManager.isTicketOwnedByUser(finalId)) {
            response = new CommandStatusResponse("Element with that id doesn't exist or you don't have permission to modify it.", 2);
            logger.warn(response.getResponse());
            return;
        }

        obj.setId(finalId); // Set ID before updating ticket in the database
        boolean updated = dbManager.updateTicket(obj); // Updating ticket in the database

        if (updated) {
            // Update the ticket in the collection
            collectionHandler.getCollection().removeIf(ticket -> Objects.equals(ticket.getId(), finalId));
            collectionHandler.addElementToCollection(obj);

            response = CommandStatusResponse.ofString("Object updated!\n finalId: " + finalId);
        } else {
            response = new CommandStatusResponse("Failed to update the object.", 2);
        }
        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse getResponse() {
        return response;
    }

    @Override
    public void setObj(Ticket obj) {
        this.obj = obj;
        obj.setId(TicketIDHandler.generateId());
        obj.setCreationDate(Date.from(Instant.now()));
    }
}
