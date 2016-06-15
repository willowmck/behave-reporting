package org.orangesoft.behave;

/**
 * Notifies when report cannot be generated.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ValidationException extends RuntimeException {

    public ValidationException(Exception e) {
        super(e);
    }

    public ValidationException(String message) {
        super(message);
    }
}
