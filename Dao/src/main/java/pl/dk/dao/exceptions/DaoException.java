package pl.dk.dao.exceptions;

import java.io.IOException;
import java.util.ResourceBundle;

public class DaoException extends IOException {

    static ResourceBundle bundle = ResourceBundle.getBundle("pl.dk.dao.Language");

    public DaoException(String message) {
        super(message);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }
}
