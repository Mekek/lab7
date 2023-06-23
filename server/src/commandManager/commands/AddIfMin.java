package commandManager.commands;

import models.Ticket;
import models.handlers.CollectionHandler_;
import models.handlers.TicketIDHandler_;
import models.handlers.TicketHandler_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse_;

import java.time.Instant;
import java.util.Date;
import java.util.Vector;

/**
 * Adds element if it's value lower than min value.
 *
 * @author Mekek
 * @since 1.0
 */
public class AddIfMin implements Command, ArgumentConsumer<Ticket> {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.addIfMin");
    private CommandStatusResponse_ response;
    private Ticket obj;

    @Override
    public String getName() {
        return "add_if_min";
    }

    @Override
    public String getDescr() {
        return "Adds element if it's value lower than min value.";
    }

    @Override
    public String getArgs() {
        return "{element}";
    }

    @Override
    public void execute(String[] args) {

//        CollectionHandler_<TreeSet<Ticket>, Ticket> collectionHandler = TicketHandler_.getInstance();
        CollectionHandler_<Vector<Ticket>, Ticket> collectionHandler = TicketHandler_.getInstance();
        if (obj.getPrice() < collectionHandler.getCollection().firstElement().getPrice()) {
            collectionHandler.addElementToCollection(obj);
            response = CommandStatusResponse_.ofString("Element added!");
        } else {
            response = new CommandStatusResponse_("Element not added: it's not lower than min value.", 3);
        }

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
