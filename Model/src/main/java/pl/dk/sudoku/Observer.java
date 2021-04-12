package pl.dk.sudoku;

import pl.dk.sudoku.exceptions.ValueException;

public interface Observer {
    void update(int value) throws ValueException;
}
