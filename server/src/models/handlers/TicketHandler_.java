package models.handlers;

import collectionStorageManager.CSVManager_;
import models.*;
import models.comparators.TicketComparator_;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.CommandStatusResponse_;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Current implementation of CollectionsHandler for HashSet of Route.
 *
 * @author Mekek
 * @since 1.0
 */
public class TicketHandler_ implements CollectionHandler_<Vector<Ticket>, Ticket> {

    private static final Logger logger = LogManager.getLogger("io.github.Mekek.lab6.manager");

    private CommandStatusResponse_ response;

    private static TicketHandler_ singletonPattern;
    private final Date initDate;

    /**
     * TreeSet collection of Ticket objects.
     */
    private Vector<Ticket> tickets;

    /**
     * Path to the data file for the collection.
     */
    private static String pathToDataFile; // c

    private TicketHandler_() {
//        tickets = new TreeSet<>(new TicketComparator_());
        tickets = new Vector<>();
        Collections.sort(tickets, new TicketComparator_());

        initDate = java.sql.Date.valueOf(LocalDate.now());
    }

    /**
     * Gets singleton instance of TicketManager.
     *
     * @return the singleton instance of TicketHandler
     */
    public static TicketHandler_ getInstance() {
        if (singletonPattern == null)
            singletonPattern = new TicketHandler_();
        return singletonPattern;
    }

    /**
     * Loads the TreeSet collection from a .csv file using the environment variable with the specified key.
     *
     * @param envKey the key of the environment variable containing the path to the .csv file
     */
    public void loadCollection(String envKey) {
        String pathToDataFile = System.getenv(envKey);
        TicketHandler_.setPathToDataFile(pathToDataFile);
        if (pathToDataFile == null) {
            System.out.println("Переменной окружения lab5 не существует!");
        } else if (pathToDataFile.trim().split(" ").length != 1) {
            System.out.println("Некорректно задана переменная окружения lab5! " +
                    "\nЗадайте переменную окружения с именем \"lab5\", поместив туда полный путь к .csv файлу.");
        } else {
            try {
                long count = 1;
                CSVManager_ csvManager = new CSVManager_();
                CollectionHandler_<Vector<Ticket>, Ticket> collectionHandler = TicketHandler_.getInstance();
                ArrayList<String> parsedCSVFile = csvManager.readFromFile(pathToDataFile);
                CSVParser csvParser = CSVParser.parse(String.join("\n", parsedCSVFile), CSVFormat.DEFAULT.withHeader("id", "name", "x", "y", "creationDate", "price", "type", "eventId", "eventName", "eventDate", "minAge"));
                for (CSVRecord fields : csvParser.getRecords()) {
                    Integer id = Integer.parseInt(fields.get("id"));
                    String name = fields.get("name");
                    Double x = Double.valueOf(fields.get("x"));
                    Float y = Float.parseFloat(fields.get("y"));
                    Coordinates_ coordinates = new Coordinates_(x, y);
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
                    response = CommandStatusResponse_.ofString(count++ + "th Element loaded!\n  fields: " + fields + "\n size: " + tickets.size());
                    logger.info(response.getResponse());
                }
                response = CommandStatusResponse_.ofString("Parsed CSV: " + tickets.size());
                logger.info(response.getResponse());
                TicketHandler_.getInstance().setCollection(collectionHandler.getCollection());

            } catch (IOException | IllegalArgumentException e) {
                throw new IllegalArgumentException("CSV format violation: " + e.getMessage());
            }
        }
    }

    public CommandStatusResponse_ getResponse() {
        return response;
    }

    /**
     * Writes TreeSet collection to .csv file
     */
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

            CSVManager_ csvManager = new CSVManager_();
            csvManager.write(pathToDataFile, header, records);
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalArgumentException("CSV format violation: " + e.getMessage());
        }
    }

    /**
     * Returns actual collection reference.
     *
     * @return Current collection
     */
    @Override
    public Vector<Ticket> getCollection() {
        return tickets;
    }

    /**
     * Overrides current collection by provided value.
     *
     * @param tickets Collection
     */
    @Override
    public void setCollection(Vector<Ticket> tickets) {
        this.tickets = tickets;
    }

    /**
     * Adds element to collection.
     *
     * @param ticket Element to add
     */
    @Override
    public void addElementToCollection(Ticket ticket) {
        if (tickets != null){
            response = CommandStatusResponse_.ofString("is element added? - " + tickets.add(ticket));
            logger.info(response.getResponse());
        } else {
//            TreeSet<Ticket> tickets = new TreeSet<>(new TicketComparator_());
//            tickets.add(ticket);
            Vector<Ticket> tickets = new Vector<>();
            tickets.add(ticket);
            tickets.sort(new TicketComparator_());
            TicketHandler_.getInstance().setCollection(tickets);
        }
    }

    /**
     * Removes all elements from the ticketTreeSet collection.
     */
    @Override
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
        if (tickets.iterator().hasNext())
            return tickets.iterator().next();
        else
            return new Ticket();
    }

    @Override
    public Date getInitDate() {
        return initDate;
    }

    /**
     * Sets the path to the .csv file.
     *
     * @param pathToDataFile the path to the .csv file to set
     */
    public static void setPathToDataFile(String pathToDataFile) {
        TicketHandler_.pathToDataFile = pathToDataFile;
    }


}
