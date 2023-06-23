package models.handlers.userMode;


import exceptions.BuildObjectException;
import main.Utilities;
import models.Event;
import models.handlers.ModeManager;
import models.validators.*;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A class that implements the ModeManager interface for creating Coordinate objects via command-line interface.
 */
public class EventCLIManager implements ModeManager<Event> {
    /**
     * Builds a Coordinates object via command-line interface, prompting the user for input.
     *
     * @author mikhail
     * @since 2.0
     * @return the created Coordinates object.
     * @throws BuildObjectException if there is an error building the object.
     */
    @Override
    public Event buildObject() throws BuildObjectException {
        try {
            Event event = new Event();
            Scanner scanner = new Scanner(System.in);
            InputValidator inputValidator = new InputValidator();
            String nextLine;
            System.out.println();

            // unique id
            event.setId(Utilities.generateId());


            // name
            String name;
            Validator<String> nameValidator = new NameValidator();
            inputValidator.canBeNull(false);
            while (true) {
                System.out.print("Enter event name(not null!)(type: String) : ");
                nextLine = scanner.nextLine();

                if (inputValidator.validate(nextLine)) {
                    name = nextLine;
                    if (nameValidator.validate(name)) {
                        event.setName(name);
                        break;
                    } else {
                        System.out.println("Value violates restrictions for field! Try again.");
                        System.out.println("Restrictions: " + nameValidator.getDescr());
                    }
                } else System.out.println("Input should not be empty!(value is not null)");
            }

            // date
            java.util.Date creationDate = java.sql.Date.valueOf(LocalDate.now());
            event.setDate(creationDate);
            // ticket.setCreationDate(Date.from(Instant.now()));

            // minAge
            int minAge;
            Validator<Integer> minAgeValidator = new MinAgeValidator();
            while (true) {
                try {
                    System.out.print("Enter minimal age(not null!)(type: int) : ");
                    nextLine = scanner.nextLine();

                    if (inputValidator.validate(nextLine)) {
                        minAge = Integer.parseInt(nextLine);
                        if (minAgeValidator.validate(minAge)) {
                            event.setMinAge(minAge);
                            break;
                        } else {
                            System.out.println("Value violates restrictions for this field! Try again.");
                            System.out.println("Restrictions: " + minAgeValidator.getDescr());
                        }
                    } else System.out.println("Input should not be empty!(value is not null)");
                } catch (InputMismatchException | NumberFormatException e) {
                    System.out.println("Wrong input! Try again.");
                }
            }

            return event;

        } catch (NoSuchElementException | NumberFormatException e) {
            throw new BuildObjectException("Во время конструирования объекта произошла ошибка: " + e.getMessage());
        }
    }
}
