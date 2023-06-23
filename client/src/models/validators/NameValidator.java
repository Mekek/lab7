package models.validators;

import models.Ticket;

/**
 * Implementation of validator for name field. (Ticket)
 *
 * @since 2.0
 * @author mikhail
 */
public class NameValidator implements Validator<String> {
    @Override
    public String getDescr() {
        return "Validates name field";
    }

    /**
     * Checks if value not null and not blank.
     *
     * @see Ticket
     * @param value name to validate
     * @return true/false -- matches the restrictions
     */
    @Override
    public boolean validate(String value) {
        return !value.isEmpty() && !value.isBlank();
    }
}