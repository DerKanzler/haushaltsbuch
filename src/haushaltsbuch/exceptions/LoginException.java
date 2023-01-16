package haushaltsbuch.exceptions;

public class LoginException extends Exception {

    private static final long serialVersionUID = -6696778198103821610L;

    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }

}