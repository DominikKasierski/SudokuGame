package pl.dk.sudoku;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dk.sudoku.exceptions.CloneException;
import pl.dk.sudoku.exceptions.ValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class SudokuElementTest {

    private static final Logger logger = LoggerFactory.getLogger(SudokuElementTest.class);

    private ResourceBundle bundle = ResourceBundle.getBundle("Language");


    @Test
    public void verifyingRow_CorrectCase() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        sudoku.solveGame();
        assertTrue(sudoku.getRow(1).verify());
    }

    @Test
    public void verifyingCol_CorrectCase() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        sudoku.solveGame();
        assertTrue(sudoku.getColumn(1).verify());
    }

    @Test
    public void verifying_IncorrectCase() {
        SudokuField field1 = new SudokuField();
        SudokuField field2 = new SudokuField();

        try {
            field1.setFieldValueWithCheck(1);
        } catch (ValueException e) {
            logger.error(bundle.getString("logger"), e);
        }
        try {
            field2.setFieldValueWithCheck(1);
        } catch (ValueException e) {
            logger.error(bundle.getString("logger"), e);
        }

        List<SudokuField> board = new ArrayList<>(2) {
        };

        board.add(field1);
        board.add(field2);

        SudokuElement sudokuElement = new SudokuElement(board);

        assertFalse(sudokuElement.verify());
    }

    @Test
    public void verifyingBox1_CorrectCase() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        sudoku.solveGame();
        assertTrue(sudoku.getBox(1, 1).verify());
    }

    @Test
    public void verifyingBox2_CorrectCase() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        sudoku.solveGame();
        assertTrue(sudoku.getBox(4, 4).verify());
    }

    @Test
    public void verifyingBox3_CorrectCase() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        sudoku.solveGame();
        assertTrue(sudoku.getBox(7, 7).verify());
    }

    @Test
    public void verifyingRowWhichIsNotFullyCompleted() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        sudoku.setWithCheck(0, 0, 1);
        sudoku.setWithCheck(0, 1, 2);
        sudoku.setWithCheck(0, 2, 3);
        assertTrue(sudoku.getRow(0).verify());
    }

    @Test
    void HashCodeCheck_IsHashCodeTheSameAllTheTime() {
        List<SudokuField> board = new ArrayList<>();
        SudokuElement sudokuElement = new SudokuElement(board);
        int hashCode1 = sudokuElement.hashCode();
        int hashCode2 = sudokuElement.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void EqualsAndHashCodeCheck_Case1() {
        SudokuField sudokuField1 = new SudokuField();
        try {
            sudokuField1.setFieldValueWithCheck(1);
        } catch (ValueException e) {
            logger.error(bundle.getString("logger"), e);
        }
        List<SudokuField> board = new ArrayList<>();
        board.add(sudokuField1);

        SudokuElement sudokuElement1 = new SudokuElement(board);
        SudokuElement sudokuElement2 = new SudokuElement(board);

        assertEquals(sudokuElement1, sudokuElement2);
        assertEquals(sudokuElement1.hashCode(), sudokuElement2.hashCode());
    }

    @Test
    void EqualsAndHashCodeCheck_Case2() {
        SudokuField sudokuField1 = new SudokuField();
        try {
            sudokuField1.setFieldValueWithCheck(1);
        } catch (ValueException e) {
            logger.error(bundle.getString("logger"), e);
        }
        SudokuField sudokuField2 = new SudokuField();
        try {
            sudokuField2.setFieldValueWithCheck(2);
        } catch (ValueException e) {
            logger.error(bundle.getString("logger"), e);
        }

        List<SudokuField> board1 = new ArrayList<>();
        board1.add(sudokuField1);

        List<SudokuField> board2 = new ArrayList<>();
        board2.add(sudokuField2);

        SudokuElement sudokuElement1 = new SudokuElement(board1);
        SudokuElement sudokuElement2 = new SudokuElement(board2);

        assertNotEquals(sudokuElement1, sudokuElement2);
        assertNotEquals(sudokuElement1.hashCode(), sudokuElement2.hashCode());
    }

    @Test
    void EqualsCheck_TestYourSelf() {
        List<SudokuField> board = new ArrayList<>();
        SudokuElement sudokuElement = new SudokuElement(board);
        assertEquals(sudokuElement, sudokuElement);
    }

    @Test
    void EqualsCheck_DifferentClasses() {
        List<SudokuField> board = new ArrayList<>();
        SudokuElement sudokuElement = new SudokuElement(board);
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        assertNotEquals(sudokuElement, sudokuBoard);
    }

    @Test
    void EqualsCheck_Null() {
        List<SudokuField> board = new ArrayList<>();
        SudokuElement sudokuElement = new SudokuElement(board);
        assertNotEquals(sudokuElement, null);
    }

    @Test
    void toStringTest() {
        List<SudokuField> board = new ArrayList<>();
        SudokuElement sudokuElement = new SudokuElement(board);
        logger.debug(sudokuElement.toString());
    }

    @Test
    void ifCloneWorksGood() {
        List<SudokuField> board = new ArrayList<>();
        SudokuElement sudokuElement = new SudokuElement(board);
        try {
            SudokuElement clonedSudokuElement = sudokuElement.clone();
            assertNotSame(sudokuElement, clonedSudokuElement);
        } catch (CloneException e) {
            logger.error(bundle.getString("logger"), e);
        }

    }

}