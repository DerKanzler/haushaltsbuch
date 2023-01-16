package haushaltsbuch.exceptions;

public class DatabaseException extends Exception {

    private static final long serialVersionUID = -6407871056507752532L;

    public DatabaseException() {
        super();
    }

    public DatabaseException(String message) {
        super(message);
    }

}