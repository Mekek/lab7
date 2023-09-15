package commandManager.commands;

import collectionStorageManager.PostgreSQLManager;
import models.Ticket;
import models.handlers.CollectionHandler;
import models.handlers.TicketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Shows every element of the collection in toString() interpretation.
 *
 * @author Mekek
 * @since 1.0
 */
public class Show implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.show");
    private CommandStatusResponse response;

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescr() {
        return "Shows every element of the collection in toString() interpretation.";
    }

    @Override
    public void execute(String[] args) {
        PostgreSQLManager manager = new PostgreSQLManager();

        logger.debug("Received args: " + Arrays.toString(args));
        int itemsPerPage = 10; // You can change this value to your desired number of items per page
        int pageNumber = 0;

        CollectionHandler<Vector<Ticket>, Ticket> collectionHandler = TicketHandler.getInstance();

        List<Ticket> ticketList = new ArrayList<>(manager.getCollectionFromDatabase());
        collectionHandler.addMissingTicketsToCollection(ticketList);
        int totalPages = (int) Math.ceil((double) ticketList.size() / itemsPerPage);

        String output;
        if (args != null && args.length > 1) {
            try {
                pageNumber = Integer.parseInt(args[1]) - 1;
            } catch (NumberFormatException e) {
                response = CommandStatusResponse.ofString("Invalid page number.");
                logger.warn(response.getResponse());
                return;
            }

            if (pageNumber < 0 || pageNumber >= totalPages) {
                response = CommandStatusResponse.ofString("Page number out of range.");
                logger.warn(response.getResponse());
                return;
            }

            output = IntStream.range(itemsPerPage * pageNumber, Math.min(itemsPerPage * (pageNumber + 1), ticketList.size()))
                    .mapToObj(i -> ticketList.get(i).toString())
                    .collect(Collectors.joining("\n"));

            response = CommandStatusResponse.ofString("Page " + (pageNumber + 1) + " of " + totalPages + ":\n" + output);
            logger.info(response.getResponse());

        } else {
            output = "Total pages: " + totalPages;
            response = CommandStatusResponse.ofString(output);
        }
    }

    @Override
    public CommandStatusResponse getResponse() {
        return response;
    }
}
