package commandManager.commands;

import models.Ticket;
import models.handlers.CollectionHandler_;
import models.handlers.TicketIDHandler_;
import models.handlers.TicketHandler_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse_;


import java.util.Vector;

/**
 * Adds new element to collection.
 *
 * @author Mekek
 * @since 1.0
 */
public class Add implements commandManager.commands.Command, commandManager.commands.ArgumentConsumer<Ticket> {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.add");
    private CommandStatusResponse_ response;

    private Ticket obj;

    @Override
    public void setObj(Ticket obj) {
        this.obj = obj;
        obj.setId(TicketIDHandler_.generateId());
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescr() {
        return "Adds new element to collection.";
    }

    @Override
    public String getArgs() {
        return "{element}";
    }

    @Override
    public void execute(String[] args) {
        CollectionHandler_<Vector<Ticket>, Ticket> collectionHandler = models.handlers.TicketHandler_.getInstance();

        collectionHandler.addElementToCollection(obj);

        response = CommandStatusResponse_.ofString("Element added!");
        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse_ getResponse() {
        return response;
    }
}
