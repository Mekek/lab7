package models.handlers;

import collectionStorageManager.CSVManager;
import collectionStorageManager.PostgreSQLManager;
import models.*;
import models.comparators.TicketComparator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Current implementation of CollectionsHandler for HashSet of Route.
 *
 * @author Mekek
 * @since 1.0
 */
public class TicketHandler implements CollectionHandler<Vector<Ticket>, Ticket> {

    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.manager");
    private final ReentrantLock lock = new ReentrantLock(true);

    private CommandStatusResponse response;

    private static TicketHandler singletonPattern;
    private final Date initDate;

    /**
     * Vector collection of Ticket objects.
     */
    private Vector<Ticket> tickets;

    /**
     * Path to the data file for the collection.
     */
    private static String pathToDataFile; // c

    private TicketHandler() {
//        tickets = new Vector<>(new TicketComparator());
        tickets = new Vector<>();
        Collections.sort(tickets, new TicketComparator());

        initDate = java.sql.Date.valueOf(LocalDate.now());
    }

    /**
     * Gets singleton instance of TicketManager.
     *
     * @return the singleton instance of TicketHandler
     */
    public static TicketHandler getInstance() {
        if (singletonPattern == null)
            singletonPattern = new TicketHandler();
        return singletonPattern;
    }

    /**
     * Loads the Vector collection from a .csv file using the environment variable with the specified key.
     *
     * @param envKey the key of the environment variable containing the path to the .csv file
     @deprecated
     */
    @Deprecated
    public void loadCollection(String envKey) {
        String pathToDataFile = System.getenv(envKey);
        TicketHandler.setPathToDataFile(pathToDataFile);
        if (pathToDataFile == null) {
            System.out.println("Переменной окружения lab6 не существует!");
        } else if (pathToDataFile.trim().split(" ").length != 1) {
            System.out.println("Некорректно задана переменная окружения lab6! " +
                    "\nЗадайте переменную окружения с именем \"lab6\", поместив туда полный путь к .csv файлу.");
        } else {
            try {
                long count = 1;
                CSVManager csvManager = new CSVManager();
                CollectionHandler<Vector<Ticket>, Ticket> collectionHandler = TicketHandler.getInstance();
                ArrayList<String> parsedCSVFile = csvManager.readFromFile(pathToDataFile);
                CSVParser csvParser = CSVParser.parse(String.join("\n", parsedCSVFile), CSVFormat.DEFAULT.withHeader("id", "name", "x", "y", "creationDate", "price", "type", "eventId", "eventName", "eventDate", "minAge"));
                for (CSVRecord fields : csvParser.getRecords()) {
                    Integer id = Integer.parseInt(fields.get("id"));
                    String name = fields.get("name");
                    Double x = Double.valueOf(fields.get("x"));
                    Float y = Float.parseFloat(fields.get("y"));
                    Coordinates coordinates = new Coordinates(x, y);
                    Date creationDate = java.sql.Date.valueOf(LocalDate.now());
                    Float price = Float.parseFloat(fields.get("price"));
                    TicketType type = TicketType.valueOf(fields.get("type"));
                    Integer eventId = Integer.parseInt(fields.get("eventId"));
                    String eventName = fields.get("eventName");
                    Date eventDate = java.sql.Date.valueOf(fields.get("eventDate"));
                    Integer minAge = Integer.parseInt(fields.get("minAge"));
                    Event event = new Event(eventId, eventName, eventDate, minAge);

                    Ticket ticket = new Ticket(id, name, coordinates, creationDate, price, type, event);
                    collectionHandler.addElementToCollection(ticket);
                    response = CommandStatusResponse.ofString(count++ + "th Element loaded!\n  fields: " + fields + "\n size: " + tickets.size());
                    logger.info(response.getResponse());
                }
                response = CommandStatusResponse.ofString("Parsed CSV: " + tickets.size());
                logger.info(response.getResponse());
                TicketHandler.getInstance().setCollection(collectionHandler.getCollection());

            } catch (IOException | IllegalArgumentException e) {
                throw new IllegalArgumentException("CSV format violation: " + e.getMessage());
            }
        }
    }

    /**
     * Loads the Vector collection.
     */
//    @Override
    public void loadCollectionFromDatabase() {
        PostgreSQLManager dbManager = new PostgreSQLManager();
        ArrayList<Ticket> tickets = dbManager.getCollectionFromDatabase();
        CollectionHandler<Vector<Ticket>, Ticket> collectionHandler = TicketHandler.getInstance();
        for (Ticket ticket: tickets) {
            collectionHandler.addElementToCollection(ticket);
        }
        collectionHandler.setCollection(collectionHandler.getCollection());
    }

    /**
     * Writes Vector collection
     * @deprecated
     */
    @Deprecated
    public void writeCollection() {
        try {
            // header of Ticket collection
            String[] header = {"id", "name", "x", "y", "creationDate", "price", "type", "eventId", "eventName",
                    "eventDate", "minAge"};

            List<String> records = new ArrayList<>();
            for (Ticket ticket : tickets) {
                records.add(ticket.getId() + "," + ticket.getName() + "," + ticket.getCoordinates().getX() + ","
                        + ticket.getCoordinates().getY() + "," + ticket.getCreationDate() + "," + ticket.getPrice() + ","
                        + ticket.getType() + "," + ticket.getEvent().getId() + "," + ticket.getEvent().getName() + ","
                        + ticket.getEvent().getDate() + "," + ticket.getEvent().getMinAge());
            }

            CSVManager csvManager = new CSVManager();
            csvManager.write(pathToDataFile, header, records);
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalArgumentException("CSV format violation: " + e.getMessage());
        }
    }

    /**
     * Writes Vector collection to Database
     */
//    @Override
    public void writeCollectionToDatabase() {
        lock.lock();
        try {
            PostgreSQLManager dbManager = new PostgreSQLManager();
            dbManager.writeCollectionToDatabase();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns actual collection reference.
     *
     * @return Current collection
     */
    @Override
    public Vector<Ticket> getCollection() {
        lock.lock();
        try {
            return tickets;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Overrides current collection by provided value.
     *
     * @param tickets Collection
     */
    @Override
    public void setCollection(Vector<Ticket> tickets) {
        lock.lock();
        try {
            this.tickets = tickets;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Adds element to collection.
     *
     * @param ticket Element to add
     */
    @Override
    public void addElementToCollection(Ticket ticket) {
        lock.lock();
        try {
            if (tickets != null)
                tickets.add(ticket);
            else {
                Vector<Ticket> tickets = new Vector<>();
                Collections.sort(tickets, new TicketComparator());
                tickets.add(ticket);
                TicketHandler.getInstance().setCollection(tickets);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes all elements from the ticketVector collection.
     */
    @Override
    @Deprecated
    public void clearCollection() {
        tickets.clear();
    }


    /**
     * Returns first element of collection.
     *
     * @return First element of collection. If collection is empty, returns new object.
     */
    @Override
    public Ticket getFirstOrNew() {
        lock.lock();
        try {
            if (tickets.iterator().hasNext())
                return tickets.iterator().next();
            else
                return new Ticket();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Date getInitDate() {
        lock.lock();
        try {
            return initDate;
        } finally {
            lock.unlock();
        }
    }

    /**
     * adds missing tickets from database to collection
     */
    @Override
    public void addMissingTicketsToCollection(List<Ticket> ticketsFromDatabase) {
        for (Ticket ticket : ticketsFromDatabase) {
            if (!this.tickets.contains(ticket)) {
                addElementToCollection(ticket);
            }
        }
    }

    /**
     * Sets the path to the .csv file.
     *
     * @param pathToDataFile the path to the .csv file to set
     */
    public static void setPathToDataFile(String pathToDataFile) {
        TicketHandler.pathToDataFile = pathToDataFile;
    }


    public CommandStatusResponse getResponse() {
        return response;
    }
}
