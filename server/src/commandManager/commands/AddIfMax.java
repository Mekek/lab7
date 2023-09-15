package commandManager.commands;

import collectionStorageManager.PostgreSQLManager;
import models.Ticket;
import models.comparators.TicketComparator;
import models.handlers.CollectionHandler;
import models.handlers.TicketIDHandler;
import models.handlers.TicketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse;

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
    private CommandStatusResponse response;
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

//        CollectionHandler<Vector<Ticket>, Ticket> collectionHandler = TicketHandler.getInstance();
        CollectionHandler<Vector<Ticket>, Ticket> collectionHandler = TicketHandler.getInstance();
        Stream<Ticket> stream = collectionHandler.getCollection().stream();

        if (obj.getPrice() > stream.sorted(new TicketComparator()).collect(Collectors.toCollection(Vector :: new)).lastElement().getPrice()) {
            PostgreSQLManager manager = new PostgreSQLManager();
            int generatedId = manager.writeObjectToDatabase(obj);

            if (generatedId != -1) {
                // Set the generated ID for the new element
                obj.setId(generatedId);
                collectionHandler.addElementToCollection(obj);
                response = CommandStatusResponse.ofString("Element added with ID: " + generatedId);
            } else response = CommandStatusResponse.ofString("Failed to add element.");
        } else response = new CommandStatusResponse("Element not added: it's not bigger than max value.", 3);
        stream = collectionHandler.getCollection().stream();
        Vector<Ticket> vector = stream.collect(Collectors.toCollection(Vector::new));
        collectionHandler.setCollection(vector);

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
