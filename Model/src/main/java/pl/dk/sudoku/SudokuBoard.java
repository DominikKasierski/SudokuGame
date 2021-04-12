package pl.dk.sudoku;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.dk.sudoku.exceptions.CloneException;

public class SudokuBoard implements Serializable, Cloneable {

    private static final int SUDOKU_SIZE = 81;

    private static final int ROWS_NUMBER = 9;

    private final SudokuSolver sudokuSolver;

    private List<SudokuField> board;

    private List<SudokuElement> rows = Arrays.asList(new SudokuElement[9]);

    private List<SudokuElement> columns = Arrays.asList(new SudokuElement[9]);

    private List<SudokuElement> boxes = Arrays.asList(new SudokuElement[9]);

    private Random random = new Random();

    /**
     * It is SudokuBoard constructor.
     *
     * @param sudokuSolver is solver which we want to use.
     */
    public SudokuBoard(final SudokuSolver sudokuSolver) {
        this.sudokuSolver = sudokuSolver;
        this.board = Stream.generate(SudokuField::new)
                .limit(81)
                .collect(Collectors.toList());
        initializeGroupsOfElements();
    }

    private void initializeGroupsOfElements() {
        initializeRowsList();
        initializeColsList();
        initializeBoxesList();
    }

    private void initializeRowsList() {
        for (int j = 0; j < ROWS_NUMBER; j++) {
            List<SudokuField> row = Arrays.asList(new SudokuField[9]);
            int rowBeginning = j * ROWS_NUMBER;

            for (int i = 0; i < ROWS_NUMBER; i++) {
                row.set(i, board.get(rowBeginning + i));
            }

            rows.set(j, new SudokuElement(row));
        }
    }

    private void initializeColsList() {
        for (int j = 0; j < ROWS_NUMBER; j++) {
            List<SudokuField> column = Arrays.asList(new SudokuField[9]);

            for (int i = 0; i < ROWS_NUMBER; i++) {
                column.set(i, board.get(j + i * ROWS_NUMBER));
            }

            columns.set(j, new SudokuElement(column));
        }
    }

    private void initializeBoxesList() {
        int sqrt = (int) Math.sqrt(ROWS_NUMBER);
        int boxRowStart;
        int boxColStart = 0;

        for (int g = 0; g < ROWS_NUMBER; g++) {
            List<SudokuField> box = Arrays.asList(new SudokuField[9]);
            boxRowStart = (g / 3) * sqrt;
            int counter = 0;
            for (int r = boxRowStart; r < boxRowStart + sqrt; r++) {
                for (int d = boxColStart; d < boxColStart + sqrt; d++) {
                    box.set(counter, board.get(r * ROWS_NUMBER + d));
                    counter++;
                }
            }

            boxes.set(g, new SudokuElement(box));

            boxColStart += sqrt;

            if (boxColStart == ROWS_NUMBER) {
                boxColStart = 0;
            }
        }
    }

    public final void solveGame() {
        sudokuSolver.solve(this);
    }

    public final int getFieldValue(final int x, final int y) {
        return board.get(x * ROWS_NUMBER + y).getFieldValue();
    }

    public final void setWithCheck(final int x, final int y, final int value) {
        board.get(x * ROWS_NUMBER + y).setFieldValueWithCheck(value);
    }

    public final void setFieldValue(final int x, final int y, final int value) {
        board.get(x * ROWS_NUMBER + y).setFieldValue(value);
    }

    public final int getSudokuSize() {
        return SUDOKU_SIZE;
    }

    public final int getRowsNumber() {
        return ROWS_NUMBER;
    }

    public final SudokuElement getRow(int x) {
        return rows.get(x);
    }

    public final SudokuElement getColumn(int y) {
        return columns.get(y);
    }

    public final SudokuElement getBox(int x, int y) {
        return boxes.get((x / 3) * 3 + (y / 3));
    }

    /**
     * It is method used to check if board is filled following the rules.
     *
     * @return true if board is filled correctly.
     */
    public boolean checkBoard() {
        return Stream.of(rows, columns, boxes)
                .flatMap(Collection::stream)
                .allMatch(SudokuElement::verify);
    }

    /**
     * It is method used to check if board is completely filled.
     *
     * @return true if board is filled.
     */
    public boolean ifBoardIsFilled() {
        for (SudokuField field : board) {
            if (field.getFieldValue() == 0) {
                return false;
            }
        }
        return true;
    }

    private int getRandomNumberInRange(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    private void deleteSomeValuesFromSudokuBoard(int numberOfFieldsToRemove) {
        int counter = 0;
        int index;

        while (counter < numberOfFieldsToRemove) {
            index = getRandomNumberInRange(0, SUDOKU_SIZE - 1);
            int x = index / ROWS_NUMBER;
            int y = index - (x * ROWS_NUMBER);
            if (getFieldValue(x, y) != 0) {
                setFieldValue(x, y, 0);
                getField(x, y).setEditable(true);
                counter++;
            }
        }
    }

    /**
     * This method change some values from board to 0.
     *
     * @param difficultyLevel tells us how many values we must change.
     */
    public void prepareInitialSudokuBoard(DifficultyLevel difficultyLevel) {
        if (difficultyLevel == DifficultyLevel.EASY) {
            deleteSomeValuesFromSudokuBoard(difficultyLevel.getNumberOfFieldsToRemove());
        }
        if (difficultyLevel == DifficultyLevel.NORMAL) {
            deleteSomeValuesFromSudokuBoard(difficultyLevel.getNumberOfFieldsToRemove());
        }
        if (difficultyLevel == DifficultyLevel.HARD) {
            deleteSomeValuesFromSudokuBoard(difficultyLevel.getNumberOfFieldsToRemove());
        }
    }

    /**
     * Method convert SudokuBoard fields values into String.
     *
     * @return string of SudokuBoard values.
     */
    public String valuesToString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ROWS_NUMBER; i++) {
            for (int j = 0; j < ROWS_NUMBER; j++) {
                builder.append(getFieldValue(i, j));
            }
        }
        return builder.toString();
    }

    /**
     * Method convert String into SudokuBoard fields values.
     */
    public void stringToValues(String values) {
        int i = 0;
        for (SudokuField field : board) {
            field.setFieldValue(Integer.parseInt(String.valueOf(values.charAt(i))));
            i++;
        }
    }

    /**
     * Method convert SudokuBoard fields editable to String.
     *
     * @return string of SudokuBoard editable.
     */
    public String editableToString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ROWS_NUMBER; i++) {
            for (int j = 0; j < ROWS_NUMBER; j++) {
                builder.append(getField(i, j).isEditable() ? 1 : 0);
            }
        }
        return builder.toString();
    }

    /**
     * Method convert String into SudokuBoard fields editable.
     */
    public void stringToEditable(String editable) {
        int i = 0;
        for (SudokuField field : board) {
            if (editable.charAt(i) == '1') {
                field.setEditable(true);
            }
            i++;
        }
    }

    /**
     * This method gives us all values from board.
     *
     * @return array of values from board.
     */
    public int[] getBoard() {
        int[] tab = new int[SUDOKU_SIZE];
        for (int i = 0; i < ROWS_NUMBER; i++) {
            for (int j = 0; j < ROWS_NUMBER; j++) {
                tab[i * ROWS_NUMBER + j] = getFieldValue(i, j);
            }
        }
        return tab;
    }

    public SudokuField getField(final int x, final int y) {
        return board.get(x * ROWS_NUMBER + y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SudokuBoard that = (SudokuBoard) o;

        return new EqualsBuilder()
                .append(board, that.board)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(sudokuSolver)
                .append(board)
                .toHashCode();
    }

    @Override public String toString() {
        return new ToStringBuilder(this)
                .append("board", getBoard())
                .toString();
    }

    /**
     * This method makes deep copy of our object.
     */
    public SudokuBoard clone() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(this);
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                 ObjectInputStream ois = new ObjectInputStream(bis)) {
                return (SudokuBoard) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            CloneException exception = new CloneException("cloneException");
            exception.initCause(e);
            throw exception;
        }
    }


}
