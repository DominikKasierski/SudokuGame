package pl.dk.sudoku.exceptions;

import java.util.ResourceBundle;

public class BoardException extends IllegalArgumentException {

    static ResourceBundle bundle = ResourceBundle.getBundle("Language");

    public BoardException(String message) {
        super(message);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }
}
