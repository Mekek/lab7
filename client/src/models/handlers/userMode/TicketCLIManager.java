package models.handlers.userMode;


import exceptions.BuildObjectException;
import main.Utilities;
import models.Ticket;
import models.handlers.ModeManager;
import models.validators.*;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This class implements the ModeManager interface for Ticket objects.
 * It contains methods for building a new Ticket object using user input through the command line interface.
 * It handles input validation for all required fields, and uses other CLIManagers to obtain nested objects.
 *
 * @author mikhail
 * @since 2.0
 * @see ModeManager
 * @see Ticket
 */
public class TicketCLIManager implements ModeManager<Ticket> {

    /**
     * Builds a new Ticket object using user input through the command line interface.
     * It handles input validation for all required fields, and uses other CLIManagers to obtain nested objects.
     *
     * @return a new Ticket object built from user input
     * @throws BuildObjectException if an error occurs during object construction
     *
     * @author mikhail
     * @since 2.0
     *
     * @see CoordinatesCLIManager
     * @see TypeCLIManager

     */
    @Override
    public Ticket buildObject() throws BuildObjectException {
        try {
            System.out.println("Building new Ticket object...");
            Ticket ticket = new Ticket();
            Scanner scanner = new Scanner(System.in);
            InputValidator inputValidator = new InputValidator();
            String nextLine;
            System.out.println();

            // unique id
            ticket.setId(Utilities.generateId());

            // name
            String name;
            Validator<String> nameValidator = new NameValidator();
            inputValidator.canBeNull(false);
            while (true) {
                System.out.print("Enter name(not null!)(type: String) : ");
                nextLine = scanner.nextLine();

                if (inputValidator.validate(nextLine)) {
                    name = nextLine;
                    if (nameValidator.validate(name)) {
                        ticket.setName(name);
                        break;
                    } else {
                        System.out.println("Value violates restrictions for field! Try again.");
                        System.out.println("Restrictions: " + nameValidator.getDescr());
                    }
                } else System.out.println("Input should not be empty!(value is not null)");
            }

            // Coordinates
            CoordinatesCLIManager coordinatesCLIHandler = new CoordinatesCLIManager();
            ticket.setCoordinates(coordinatesCLIHandler.buildObject());

            // date
            java.util.Date creationDate = java.sql.Date.valueOf(LocalDate.now());
            ticket.setCreationDate(creationDate);
            // ticket.setCreationDate(Date.from(Instant.now()));

            // price
//            Float price;
            Validator<Float> priceValidator = new PriceValidator();
            while (true) {
                try {
                    System.out.print("Enter price(not null!)(type: Float) : ");
                    nextLine = scanner.nextLine();

                    if (inputValidator.validate(nextLine)) {
                        Float price = Float.valueOf(nextLine);
                        if (priceValidator.validate(price)) {
                            ticket.setPrice(price);
                            break;
                        } else {
                            System.out.println("Value violates restrictions for this field! Try again.");
                            System.out.println("Restrictions: " + priceValidator.getDescr());
                        }
                    } else System.out.println("Input should not be empty!(value is not null)");
                } catch (InputMismatchException | NumberFormatException e) {
                    System.out.println("Wrong input! Try again.");
                }
            }

            // ticketType
            TypeCLIManager climateCLIManager = new TypeCLIManager();
            ticket.setType(climateCLIManager.buildObject());

            // event
            EventCLIManager eventCLIManager = new EventCLIManager();
            ticket.setEvent(eventCLIManager.buildObject());

            return ticket;

        } catch (NoSuchElementException e) {
            throw new BuildObjectException("Во время конструирования объекта произошла ошибка: " + e.getMessage());
        }
    }
}
