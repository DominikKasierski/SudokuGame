package pl.dk.sudoku;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.dk.sudoku.exceptions.CloneException;
import pl.dk.sudoku.exceptions.ValueException;

public class SudokuField implements Observable, Serializable, Cloneable, Comparable<SudokuField> {

    private int fieldValue;

    private boolean editable = false;

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 9;

    private List<Observer> observers = new ArrayList<>();

    public int getFieldValue() {
        return fieldValue;
    }

    /**
     * This method set value if it is unique among the groups (row, col, square).
     *
     * @param valueToPut is value which we want to put.
     */
    public void setFieldValueWithCheck(int valueToPut) {

        if (valueToPut < MIN_VALUE || valueToPut > MAX_VALUE) {
            throw new ValueException("rangeException");
        }
        if (valueToPut != 0) {
            notifyObservers(valueToPut);
        }
        this.fieldValue = valueToPut;
    }

    public void setFieldValue(int valueToPut) {
        this.fieldValue = valueToPut;
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(int valueToPut) {
        observers.forEach(ob -> {
            ob.update(valueToPut);
        });
    }

    public int getNumberOfObservers() {
        return observers.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SudokuField that = (SudokuField) o;
        return new EqualsBuilder()
                .append(fieldValue, that.fieldValue)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(fieldValue)
                .toHashCode();
    }

    @Override public String toString() {
        return new ToStringBuilder(this)
                .append("value", fieldValue)
                .toString();
    }

    /**
     * This method makes deep copy of our object.
     */
    public SudokuField clone() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(this);
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                 ObjectInputStream ois = new ObjectInputStream(bis)) {
                return (SudokuField) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            CloneException exception = new CloneException("cloneException");
            exception.initCause(e);
            throw exception;
        }
    }

    @Override public int compareTo(SudokuField o) throws NullPointerException {
        if (o == null) {
            throw new NullPointerException();
        }
        return Integer.compare(this.fieldValue, o.fieldValue);
    }

}
