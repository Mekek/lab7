package models.comparators;

import models.Ticket;

import java.util.Comparator;

/**
 * This class provides a comparator for the Ticket class that compares two tickets
 * based on their natural order using the compareTo method of the Ticket class.
 */
public class TicketComparator implements Comparator<Ticket> {

    /**
     * Compares two Ticket objects based on their natural order using the compareTo method.
     * @param o1 the first Ticket object to be compared
     * @param o2 the second Ticket object to be compared
     * @return a negative integer, zero, or a positive integer as the first argument
     *         is less than, equal to, or greater than the second.
     */
    @Override
    public int compare(Ticket o1, Ticket o2) {
        if (o1 == null) {
            if (o2 == null) {
                return 0;
            } else {
                return -1;
            }
        }
        return o1.compareTo(o2);
    }
}
