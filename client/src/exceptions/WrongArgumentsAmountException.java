package exceptions;

public class WrongArgumentsAmountException extends Exception {
    public WrongArgumentsAmountException(String msg)
    {
        super(msg);
    }
}
