package commandManager.commands;

import models.Ticket;
import models.comparators.TicketComparatorByEvent;
import models.handlers.CollectionHandler;
import models.handlers.TicketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse;

import java.util.Vector;
import java.util.stream.Stream;

/**
 * Prints all distance fields in ascending sorting.
 *
 * @author Mekek
 * @since 1.0
 */
public class PrintFieldAscendingEvent implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.printFieldAscendingEvent");
    private CommandStatusResponse response;

    @Override
    public String getName() {
        return "print_field_ascending_event";
    }

    @Override
    public String getDescr() {
        return "Prints all distance fields in ascending sorting.";
    }

    @Override
    public void execute(String[] args) {
        CollectionHandler<Vector<Ticket>, Ticket> collectionHandler = TicketHandler.getInstance();

        Stream<Ticket> stream = collectionHandler.getCollection().stream();
        stream = stream.sorted(new TicketComparatorByEvent());
        StringBuilder sb = new StringBuilder();
        stream.forEach(ticket -> sb.append(ticket.getEvent().getName()).append('\n'));


        response = CommandStatusResponse.ofString(sb.toString());

        if (collectionHandler.getCollection().isEmpty())
            response = CommandStatusResponse.ofString("There's nothing to show...");

        logger.info(response.getResponse());
    }


    @Override
    public CommandStatusResponse getResponse() {
        return response;
    }
}
