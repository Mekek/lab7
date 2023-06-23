package models;

import java.io.Serializable;

/**
 * @author Mikhail Kadilov
 * The 'TicketType' class represents the different possible types of tickets
 */
public enum TicketType implements Serializable {
    VIP,
    USUAL,
    BUDGETARY,
    CHEAP
}