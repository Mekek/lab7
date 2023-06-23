package models.validators;

import models.Coordinates_;
import models.Ticket;

import java.util.Optional;

/**
 * Implementation of validator for Ticket
 *
 *  @since 2.0
 *  @author mikhail
 */
public class TicketValidator implements Validator<Ticket>{
    @Override
    public boolean validate(Ticket ticket) {
        return new NameValidator().validate(ticket.getName())
                && new CoordinateXValidator().validate(Optional.of(ticket).map(Ticket::getCoordinates).map(Coordinates_::getX).orElse(null))
                && new CoordinateYValidator().validate(Optional.of(ticket).map(Ticket::getCoordinates).map(Coordinates_::getY).orElse(null))
                && new PriceValidator().validate(ticket.getPrice());

    }

    @Override
    public String getDescr() {
        return "Validates Ticket object";
    }
}
