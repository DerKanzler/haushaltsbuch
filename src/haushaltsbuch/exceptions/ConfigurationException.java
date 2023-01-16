package haushaltsbuch.exceptions;

public class ConfigurationException extends Exception {

    private static final long serialVersionUID = 3038591260609655996L;

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String message) {
        super(message);
    }

}