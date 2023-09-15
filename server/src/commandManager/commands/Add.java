package commandManager.commands;

import collectionStorageManager.PostgreSQLManager;
import models.Ticket;
import models.comparators.TicketComparator;
import models.handlers.CollectionHandler;
import models.handlers.TicketHandler;
import models.handlers.TicketIDHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse;


import java.util.Vector;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Adds new element to collection.
 *
 * @author Mekek
 * @since 1.0
 */
public class Add implements commandManager.commands.Command, commandManager.commands.ArgumentConsumer<Ticket> {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.add");
    private CommandStatusResponse response;

    private Ticket obj;

    @Override
    public void setObj(Ticket obj) {
        this.obj = obj;
        obj.setId(TicketIDHandler.generateId());
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
        PostgreSQLManager manager = new PostgreSQLManager();

        // Add the new element to the database and retrieve its generated ID
        int generatedId = manager.writeObjectToDatabase(obj);

        if (generatedId != -1) {
            // Set the generated ID for the new element
            obj.setId(generatedId);
            CollectionHandler<Vector<Ticket>, Ticket> collectionHandler = TicketHandler.getInstance();
            collectionHandler.addElementToCollection(obj);
            Stream<Ticket> stream = collectionHandler.getCollection().stream().sorted(new TicketComparator());
            Vector<Ticket> vector = stream.collect(Collectors.toCollection(Vector::new));
            collectionHandler.setCollection(vector);

            response = CommandStatusResponse.ofString("Element added with ID: " + generatedId);
        } else {
            response = CommandStatusResponse.ofString("Failed to add element.");
        }

        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse getResponse() {
        return response;
    }
}
