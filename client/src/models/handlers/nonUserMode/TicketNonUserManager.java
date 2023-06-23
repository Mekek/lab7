package models.handlers.nonUserMode;


import exceptions.BuildObjectException;
import main.Utilities;
import models.*;
import models.handlers.ModeManager;
import models.validators.TicketValidator;
import models.validators.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A ModeManager implementation for generating Ticket objects without user input.
 */
public class TicketNonUserManager implements ModeManager<Ticket> {
    private static final Logger myLogger = Logger.getLogger("com.github.Mekek.lab5");

    Scanner scanner;

    /**
     * Constructor for setup handler's scanner.
     *
     * @param scanner Command scanner for reading argument
     */
    public TicketNonUserManager(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Generates a Ticket object by reading values from the scanner and sets its attributes accordingly.
     *
     * @return A generated Ticket object.
     * @throws BuildObjectException If any of the generated values violate restrictions and cannot be added to the collection.
     */
    @Override
    public Ticket buildObject() throws BuildObjectException {
        System.out.println("Generating object...");
        Ticket result = new Ticket();
        int valuesToRead = 10;
        ArrayList<String> values = new ArrayList<>(valuesToRead);

        for (int i = 0; i < valuesToRead && scanner.hasNextLine(); i++) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty())
                values.add(line);
            else
                values.add(null);
        }

        try {
            // id
            result.setId(Utilities.generateId());

            // name
            result.setName(values.get(0));
            System.out.println("Name: " + result.getName());
            System.out.println();

            // coordinates
//            System.out.println("Generating coordinates...");
            Coordinates_ coordinates = new Coordinates_();
            coordinates.setX(Double.parseDouble(values.get(1)));
            System.out.println("Coords X: " + coordinates.getX());
            coordinates.setY(Float.parseFloat(values.get(2)));
            System.out.println("Coords Y: " + coordinates.getY());
            result.setCoordinates(coordinates);
            System.out.println("Coords: " + result.getCoordinates());
            System.out.println();

            // date
            java.util.Date creationDate = java.sql.Date.valueOf(LocalDate.now());
            result.setCreationDate(creationDate);
            // result.setCreationDate(Date.from(Instant.now()));
            System.out.println("Generated at: " + result.getCreationDate());
            System.out.println("Object generated! Validating result...");
            System.out.println();

            // price
            result.setPrice(Float.valueOf(values.get((3))));
            System.out.println("Price: " + result.getPrice());
            System.out.println();

            // ticketType
            int i = TicketType.values().length;
            int userAnswer = Integer.parseInt(values.get(4));
            if (userAnswer >= 1 && userAnswer <= i)
                result.setType(TicketType.values()[userAnswer - 1]);
            else {
                System.out.println("Object's invalid, skipping...");
                throw new BuildObjectException("Созданный элемент нарушает ограничения и не может быть добавлен в коллекцию!");
            }
            System.out.println("Ticket type: " + TicketType.values()[userAnswer - 1]);


            // Event
            Event event = new Event();

            // eventId
            event.setId(Utilities.generateId());

            // eventName
            event.setName(values.get(5));

            // date
            java.util.Date eventDate = java.sql.Date.valueOf(LocalDate.now());
            result.setCreationDate(eventDate);

            // minAge
            event.setMinAge(Integer.parseInt(values.get((6))));
            System.out.println("MinAge: " + event.getMinAge());
            System.out.println();


            result.setEvent(event);
            System.out.println("Event: " + event.getName());
            System.out.println();

            Validator<Ticket> validator = new TicketValidator();
            if (!validator.validate(result)) {
                myLogger.log(Level.WARNING,"Object's invalid, skipping...");
                throw new BuildObjectException("Созданный элемент нарушает ограничения и не может быть добавлен в коллекцию!");
            }
            System.out.println("Validate successful! Sending result...");

            return result;

        } catch (NumberFormatException | NullPointerException e) {
            throw new BuildObjectException("Предоставленные данные для построения объекта неверны. Воспользуйтесь ручным вводом или исправьте команду в скрипте.");
        }
    }
}
