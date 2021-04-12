package pl.dk.sudoku;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.dk.sudoku.exceptions.CloneException;
import pl.dk.sudoku.exceptions.ValueException;

public class SudokuElement implements Observer, Serializable, Cloneable {

    private List<SudokuField> board;

    public SudokuElement(final List<SudokuField> board) {
        this.board = board;
        this.board.forEach(ob -> ob.attach(this));
    }

    /**
     * This method check if all values in group are unique.
     *
     * @return true if all values are distinct.
     */
    public final boolean verify() {
        for (int i = 1; i < 10; i++) {
            int counter = 0;
            for (SudokuField sudokuField : board) {
                if (sudokuField.getFieldValue() == i) {
                    counter++;
                }
                if (counter > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void update(int valueToPut) {
        for (SudokuField sudokuField : board) {
            if (sudokuField.getFieldValue() == valueToPut) {
                throw new ValueException("usedValueException");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SudokuElement that = (SudokuElement) o;

        return new EqualsBuilder()
                .append(board, that.board)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(board)
                .toHashCode();
    }

    @Override public String toString() {
        return new ToStringBuilder(this)
                .append("board", board)
                .toString();
    }

    /**
     * This method makes deep copy of our object.
     */
    public SudokuElement clone() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(this);
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                 ObjectInputStream ois = new ObjectInputStream(bis)) {
                return (SudokuElement) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            CloneException exception = new CloneException("cloneException");
            exception.initCause(e);
            throw exception;
        }
    }
}
