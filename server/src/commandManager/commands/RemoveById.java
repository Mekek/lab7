package commandManager.commands;

import models.Ticket;
import models.handlers.TicketHandler_;
import models.handlers.CollectionHandler_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse_;

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
    private CommandStatusResponse_ response;

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
        CollectionHandler_<Vector<Ticket>, Ticket> collectionHandler = TicketHandler_.getInstance();
        long countBefore = collectionHandler.getCollection().stream().count();
        Stream<Ticket> stream = collectionHandler.getCollection().stream();
        stream = stream.filter(ticket -> !Objects.equals(ticket.getId(), Integer.valueOf(args[1])));
        long countAfter = stream.count();
        Vector<Ticket> vector = stream.collect(Collectors.toCollection(Vector::new));
        collectionHandler.setCollection(vector);

        if (countBefore != countAfter)
            response = CommandStatusResponse_.ofString("Executed.");
        else
            response = CommandStatusResponse_.ofString("Element with that id doesn't exists.");

        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse_ getResponse() {
        return response;
    }
}
