package commandManager.commands;

import models.Ticket;
import models.handlers.CollectionHandler;
import models.handlers.TicketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse;

import java.util.Objects;
import java.util.Vector;
import java.util.Vector;
import java.util.stream.Stream;

public class CountByPrice_ implements Command {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.commands.countByPrice");
    private CommandStatusResponse response;

    @Override
    public String getName() {
        return "count_by_price";
    }

    @Override
    public String getDescr() {
        return "Prints number of elements in collection with the same price.";
    }

    @Override
    public String getArgs() {
        return "price";
    }


    @Override
    public void execute(String[] args) {

        CollectionHandler<Vector<Ticket>, Ticket> collectionHandler = TicketHandler.getInstance();

        Vector<Ticket> ticketCollection = collectionHandler.getCollection();

        if (ticketCollection.isEmpty()) {
            response = CommandStatusResponse.ofString("There's no elements in the collection");
        } else {
            long amount = ticketCollection.stream()
                    .filter(ticket -> Objects.equals(ticket.getPrice(), Float.valueOf(args[1])))
                    .count();
            response = CommandStatusResponse.ofString("Amount: " + amount);
        }

        logger.info(response.getResponse());
    }

    @Override
    public CommandStatusResponse getResponse() {
        return response;
    }
}
