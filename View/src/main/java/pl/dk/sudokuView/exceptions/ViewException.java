package pl.dk.sudokuView.exceptions;

import java.io.IOException;
import java.util.ResourceBundle;

public class ViewException extends IOException {

    static ResourceBundle bundle = ResourceBundle.getBundle("pl.dk.sudokuView.Language");

    public ViewException(String message) {
        super(message);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }
}
