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

class SudokuFieldTest {

    private static final Logger logger = LoggerFactory.getLogger(SudokuFieldTest.class);

    private ResourceBundle bundle = ResourceBundle.getBundle("Language");

    @Test
    void getAndSetFieldValue() {
        SudokuField sudokuField = new SudokuField();
        try {
            sudokuField.setFieldValueWithCheck(5);
        } catch (ValueException e) {
            logger.error(bundle.getString("logger"), e);
        }
        assertEquals(5, sudokuField.getFieldValue());
    }

    @Test
    void addAndDetachObserver() {
        SudokuField sudokuField = new SudokuField();
        List<SudokuField> list = new ArrayList<>();

        sudokuField.attach(new SudokuElement(list));
        assertEquals(sudokuField.getNumberOfObservers(), 1);

        sudokuField.detach(new SudokuElement(list));
        assertEquals(sudokuField.getNumberOfObservers(), 0);
    }

    @Test
    void SetFieldValue_IncorrectCase1() {
        SudokuField sudokuField = new SudokuField();
        assertThrows(ValueException.class, () -> sudokuField.setFieldValueWithCheck(-5));
    }

    @Test
    void SetIncorrectFieldValue__IncorrectCase2() {
        SudokuField sudokuField = new SudokuField();
        assertThrows(ValueException.class, () -> sudokuField.setFieldValueWithCheck(10));
    }

    @Test
    void HashCodeCheck_IsHashCodeTheSameAllTheTime() {
        SudokuField sudokuField1 = new SudokuField();
        int hashCode1 = sudokuField1.hashCode();
        int hashCode2 = sudokuField1.hashCode();
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
        SudokuField sudokuField2 = new SudokuField();
        try {
            sudokuField2.setFieldValueWithCheck(1);
        } catch (ValueException e) {
            logger.error(bundle.getString("logger"), e);
        }
        assertEquals(sudokuField1, sudokuField2);
        assertEquals(sudokuField1.hashCode(), sudokuField2.hashCode());
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
        assertNotEquals(sudokuField1, sudokuField2);
        assertNotEquals(sudokuField1.hashCode(), sudokuField2.hashCode());
    }

    @Test
    void EqualsCheck_TestYourSelf() {
        SudokuField sudokuField1 = new SudokuField();
        assertEquals(sudokuField1, sudokuField1);
    }

    @Test
    void EqualsCheck_DifferentClasses() {
        SudokuField sudokuField1 = new SudokuField();
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        assertNotEquals(sudokuField1, sudokuBoard);
    }

    @Test
    void EqualsCheck_Null() {
        SudokuField sudokuField1 = new SudokuField();
        assertNotEquals(sudokuField1, null);
    }

    @Test
    void toStringTest() {
        SudokuField sudokuField = new SudokuField();
        logger.debug(sudokuField.toString());
    }

    @Test
    void ifCloneWorksGood() {
        SudokuField sudokuField = new SudokuField();
        try {
            SudokuField clonedSudokuField = sudokuField.clone();
            assertNotSame(sudokuField, clonedSudokuField);
        } catch (CloneException e) {
            logger.error(bundle.getString("logger"), e);
        }
    }

    @Test
    void compareToTest() {
        SudokuField sudokuField1 = new SudokuField();
        sudokuField1.setFieldValue(1);
        SudokuField sudokuField2 = new SudokuField();
        sudokuField2.setFieldValue(2);
        assertEquals(sudokuField1.compareTo(sudokuField2), -1);
    }

    @Test
    void compareToNullTest() {
        SudokuField sudokuField1 = new SudokuField();
        sudokuField1.setFieldValue(1);
        assertThrows(NullPointerException.class, () -> sudokuField1.compareTo(null));
    }

    @Test
    void editableTest() {
        SudokuField sudokuField = new SudokuField();
        sudokuField.setEditable(true);
        assertTrue(sudokuField.isEditable());
    }
}