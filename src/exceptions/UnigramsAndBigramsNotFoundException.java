package exceptions;

/**
 * Created by Francesco on 08/02/2017.
 */
public class UnigramsAndBigramsNotFoundException extends Exception {
    public UnigramsAndBigramsNotFoundException(String message) {
        super(message);
    }
    public UnigramsAndBigramsNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
