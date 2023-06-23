package main;

import exceptions.WrongArgumentsAmountException;

import java.util.concurrent.ThreadLocalRandom;

public class Utilities {
    public static void checkArgumentsOrThrow(int provided, int expected) throws WrongArgumentsAmountException {
        if (provided - 1 != expected)
            throw new WrongArgumentsAmountException("Provided " + (provided - 1) + " arguments, expected " + expected);
    }

    /**
     * Generates a unique ID based on threads
     *
     * @return the unique ID that was generated
     */
    public static Integer generateId() {
        Integer id;
        id = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
        return id;
    }
}
