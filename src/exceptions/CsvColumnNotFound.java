package exceptions;

/**
 * Created by Francesco on 28/01/2017.
 */
public class CsvColumnNotFound extends Exception{

        public CsvColumnNotFound(String message) {
            super(message);
        }

        public CsvColumnNotFound(String message, Throwable throwable) {
            super(message, throwable);
        }
}
