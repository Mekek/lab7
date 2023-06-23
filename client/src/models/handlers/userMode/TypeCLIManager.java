package models.handlers.userMode;


import exceptions.BuildObjectException;
import models.TicketType;
import models.handlers.ModeManager;
import models.validators.InputValidator;

import java.util.Scanner;

/**
 * The TypeCLIManager class is responsible for managing the Type mode in the user interface.
 * It implements the ModeManager interface for Type type and provides methods for building the object.
 *
 * @author mikhail
 * @since 2.0
 */
public class TypeCLIManager implements ModeManager<TicketType> {
    /**
     * This method builds the Type object using user input and returns the constructed object.
     *
     * @return The Type object built using user input.
     * @throws BuildObjectException If there is an error while constructing the object.
     */
    @Override
    public TicketType buildObject() throws BuildObjectException {
        Scanner scanner = new Scanner(System.in);
        InputValidator inputValidator = new InputValidator();
        System.out.println();

        inputValidator.canBeNull(true);
        EnumRequester<TicketType> enumRequester = new EnumRequester<>();
        TicketType ticketType = enumRequester.requestEnum(TicketType.values(), TicketType.class.getSimpleName(), scanner, inputValidator);
        return ticketType;
    }
}
