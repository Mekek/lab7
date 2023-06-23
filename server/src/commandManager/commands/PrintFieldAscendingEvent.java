package commandManager.commands;

import models.Ticket;
import models.comparators.TicketComparatorByEvent_;
import models.handlers.CollectionHandler_;
import models.handlers.TicketHandler_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse_;

import java.util.Vector;

/**
 * Prints all distance fields in ascending sorting.
 *
 * @author Mekek
 * @since 1.0
 */
public class PrintFieldAscendingEvent implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.printFieldAscendingEvent");
    private CommandStatusResponse_ response;

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
//        CollectionHandler_<TreeSet<Ticket>, Ticket> collectionHandler = TicketHandler_.getInstance();
//
//        TreeSet<Ticket> sortedTickets = new TreeSet<>(new TicketComparatorByEvent_().reversed());

        CollectionHandler_<Vector<Ticket>, Ticket> collectionHandler = TicketHandler_.getInstance();
        Vector<Ticket> sortedTickets = new Vector<>();
        sortedTickets.sort(new TicketComparatorByEvent_());

        sortedTickets.addAll(collectionHandler.getCollection());

        StringBuilder sb = new StringBuilder();
        for (Ticket ticket : sortedTickets) {
            sb.append(ticket.getEvent().getName()).append('\n');
        }
        response = CommandStatusResponse_.ofString(sb.toString());

        if (collectionHandler.getCollection().isEmpty())
            response = CommandStatusResponse_.ofString("There's nothing to show...");

        logger.info(response.getResponse());
    }


    @Override
    public CommandStatusResponse_ getResponse() {
        return response;
    }
}
