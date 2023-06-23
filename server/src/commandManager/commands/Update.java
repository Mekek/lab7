package commandManager.commands;

import models.Ticket;
import models.handlers.TicketIDHandler_;
import models.handlers.CollectionHandler_;
import models.handlers.TicketHandler_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse_;

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
    private CommandStatusResponse_ response;
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
        CollectionHandler_<Vector<Ticket>, Ticket> collectionHandler = TicketHandler_.getInstance();

        Integer finalId = Integer.valueOf(args[1]);

        if (!collectionHandler.getCollection().removeIf(ticket -> Objects.equals(ticket.getId(), finalId))) {
            response = new CommandStatusResponse_("Element with that id doesn't exists.", 2);
            logger.warn(response.getResponse());
            return;
        }

        logger.info("Updated ID value: " + finalId);
        obj.setId(finalId);

        collectionHandler.addElementToCollection(obj);

        response = CommandStatusResponse_.ofString("Object updated!");
        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse_ getResponse() {
        return response;
    }

    @Override
    public void setObj(Ticket obj) {
        this.obj = obj;
        obj.setId(TicketIDHandler_.generateId());
        obj.setCreationDate(Date.from(Instant.now()));
    }
}
