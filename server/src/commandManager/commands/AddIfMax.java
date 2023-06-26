package commandManager.commands;

import models.Ticket;
import models.comparators.TicketComparator_;
import models.handlers.CollectionHandler_;
import models.handlers.TicketIDHandler_;
import models.handlers.TicketHandler_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse_;

import java.time.Instant;
import java.util.Date;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Adds element if it's value lower than min value.
 *
 * @author Mekek
 * @since 1.0
 */
public class AddIfMax implements Command, ArgumentConsumer<Ticket> {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.addIfMax");
    private CommandStatusResponse_ response;
    private Ticket obj;

    @Override
    public String getName() {
        return "add_if_max";
    }

    @Override
    public String getDescr() {
        return "Adds element if it's value bigger than max value.";
    }

    @Override
    public String getArgs() {
        return "{element}";
    }

    @Override
    public void execute(String[] args) {

//        CollectionHandler_<TreeSet<Ticket>, Ticket> collectionHandler = TicketHandler_.getInstance();
        CollectionHandler_<Vector<Ticket>, Ticket> collectionHandler = TicketHandler_.getInstance();
        Stream<Ticket> stream = collectionHandler.getCollection().stream();

        if (obj.getPrice() > stream.sorted(new TicketComparator_()).collect(Collectors.toCollection(Vector :: new)).lastElement().getPrice()) {
            collectionHandler.addElementToCollection(obj);
            response = CommandStatusResponse_.ofString("Element added!");
        } else {
            response = new CommandStatusResponse_("Element not added: it's not bigger than max value.", 3);
        }
        stream = collectionHandler.getCollection().stream();
        Vector<Ticket> vector = stream.collect(Collectors.toCollection(Vector::new));
        collectionHandler.setCollection(vector);

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
