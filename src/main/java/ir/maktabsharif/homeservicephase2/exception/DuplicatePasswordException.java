package ir.maktabsharif.homeservicephase2.exception;

public class DuplicatePasswordException extends RuntimeException {

    public DuplicatePasswordException(String message) {
        super(message);
    }
}
