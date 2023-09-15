package models.handlers;

import models.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class TicketIDHandler {
    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6");

    /**
     * A queue to keep track of IDs that have been generated
     */
    private static Queue<Integer> queue;

    /**
     * Initializes the queue for ID storage
     */
    static {
        queue = new LinkedList<>();
    }

    /**
     * Checks if a Ticket object with the given ID exists in the collection, and if so, returns it
     *
     * @param id the ID to check
     * @return the Ticket object with the specified ID if it exists, otherwise null
     * @deprecated wrote better checking in removeByIdCommand
     */
    @Deprecated
    public static Ticket checkTicketById(Integer id) {
        CollectionHandler<Vector<Ticket>, Ticket> collectionHandler = TicketHandler.getInstance();
        for (Ticket ticket : collectionHandler.getCollection()) {
            if (ticket.getId() == id)
                return ticket;
        }
        return null;
    }

    /**
     * Generates a unique ID that has not been previously generated and stores it in the queue
     *
     * @return the unique ID that was generated
     */
    public static Integer generateId() {
        Integer id;
        do {
            id = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
        } while (queue.contains(id));
        queue.add(id);
        return id;
    }
}
