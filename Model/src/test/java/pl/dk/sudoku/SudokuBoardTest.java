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

class SudokuBoardTest {

    private ResourceBundle bundle = ResourceBundle.getBundle("Language");

    private static final Logger logger = LoggerFactory.getLogger(SudokuBoardTest.class);

    @Test
    public void getSudokuSize() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        assertEquals(sudoku.getSudokuSize(), 81);
    }

    @Test
    public void setAndGetValue() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        sudoku.getField(1,1).setFieldValue(1);
        assertEquals(1, sudoku.getField(1, 1).getFieldValue());
    }

    @Test
    public void prepareInitialBoardEasy() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        sudoku.solveGame();
        sudoku.prepareInitialSudokuBoard(DifficultyLevel.EASY);
        assertFalse(sudoku.ifBoardIsFilled());
        assertTrue(sudoku.checkBoard());
    }

    @Test
    public void prepareInitialBoardNormal() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        sudoku.solveGame();
        sudoku.prepareInitialSudokuBoard(DifficultyLevel.NORMAL);
        assertFalse(sudoku.ifBoardIsFilled());
        assertTrue(sudoku.checkBoard());
    }

    @Test
    public void prepareInitialBoardHard() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        sudoku.solveGame();
        sudoku.prepareInitialSudokuBoard(DifficultyLevel.HARD);
        assertFalse(sudoku.ifBoardIsFilled());
        assertTrue(sudoku.checkBoard());
    }

    @Test
    public void setValueAndGetThisValue() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        sudoku.setWithCheck(1, 5, 5);
        assertEquals(5, sudoku.getFieldValue(1, 5));
    }

    @Test
    public void tryingToSetIncorrectValueRow_ListenerCheck() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        sudoku.setWithCheck(0, 0, 5);
        Exception exception = assertThrows(ValueException.class, () -> sudoku.setWithCheck(0, 1, 5));

        String expectedMessage = bundle.getString("usedValueException");
        String actualMessage = exception.getLocalizedMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void tryingToSetIncorrectValueColumn_ListenerCheck() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        sudoku.setWithCheck(1, 5, 5);
        Exception exception = assertThrows(ValueException.class, () -> sudoku.setWithCheck(2, 5, 5));

        String expectedMessage = bundle.getString("usedValueException");
        String actualMessage = exception.getLocalizedMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void tryingToSetIncorrectValueBox_ListenerCheck() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());
        sudoku.setWithCheck(0, 0, 5);
        Exception exception = assertThrows(ValueException.class, () -> sudoku.setWithCheck(1, 1, 5));

        String expectedMessage = bundle.getString("usedValueException");
        String actualMessage = exception.getLocalizedMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void HashCodeCheck_IsHashCodeTheSameAllTheTime() {
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        int hashCode1 = sudokuBoard.hashCode();
        int hashCode2 = sudokuBoard.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void EqualsAndHashCodeCheck_Case1() {
        SudokuBoard sudokuBoard1 = new SudokuBoard(new BacktrackingSudokuSolver());
        sudokuBoard1.solveGame();
        SudokuBoard sudokuBoard2 = new SudokuBoard(new BacktrackingSudokuSolver());
        assertNotEquals(sudokuBoard1, sudokuBoard2);
        assertNotEquals(sudokuBoard1.hashCode(), sudokuBoard2.hashCode());
    }

    @Test
    void EqualsCheck_TestYourSelf() {
        SudokuBoard sudokuBoard1 = new SudokuBoard(new BacktrackingSudokuSolver());
        assertEquals(sudokuBoard1, sudokuBoard1);
    }

    @Test
    void EqualsCheck_DifferentClasses() {
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        List<SudokuField> board = new ArrayList<>();
        SudokuElement sudokuElement = new SudokuElement(board);
        assertNotEquals(sudokuBoard, sudokuElement);
    }

    @Test
    void EqualsCheck_Null() {
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        assertNotEquals(sudokuBoard, null);
    }


    @Test
    void toStringTest() {
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        logger.debug(sudokuBoard.toString());
    }

    @Test
    void ifCloneWorksGood() {
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        sudokuBoard.solveGame();
        assertTrue(sudokuBoard.ifBoardIsFilled());
        try {
            SudokuBoard clonedSudokuBoard = sudokuBoard.clone();
            assertNotSame(sudokuBoard, clonedSudokuBoard);
            assertNotSame(sudokuBoard.getRow(1), clonedSudokuBoard.getRow(1));
            assertNotSame(sudokuBoard.getColumn(1), clonedSudokuBoard.getColumn(1));
            assertNotSame(sudokuBoard.getBox(1, 1), clonedSudokuBoard.getBox(1, 1));
        } catch (CloneException e) {
            logger.error(bundle.getString("logger"), e);
        }
    }
}