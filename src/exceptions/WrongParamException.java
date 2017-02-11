package exceptions;

/**
 * Created by Francesco on 11/02/2017.
 */
public class WrongParamException  extends Exception{

        public WrongParamException(String message) {
            super(message);
        }

        public WrongParamException(String message, Throwable throwable) {
            super(message, throwable);
        }
}
