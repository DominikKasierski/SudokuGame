package pl.dk.dao;

import org.junit.jupiter.api.Test;
import pl.dk.dao.exceptions.DaoException;
import pl.dk.dao.exceptions.FileDaoException;
import pl.dk.sudoku.BacktrackingSudokuSolver;
import pl.dk.sudoku.SudokuBoard;

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class FileSudokuBoardDaoTest {

    private ResourceBundle bundle = ResourceBundle.getBundle("pl.dk.dao.Language");

    @Test
    void writeAndReadUsingFactory() throws DaoException {
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        sudokuBoard.solveGame();
        Dao<SudokuBoard> sudokuBoardDao = SudokuBoardDaoFactory.getFileDao("D:\\file.txt");
        sudokuBoardDao.write(sudokuBoard);
        SudokuBoard sudokuBoard1 = sudokuBoardDao.read();
        assertEquals(sudokuBoard, sudokuBoard1);
    }

    @Test
    void readException() {
        Dao<SudokuBoard> sudokuBoardDao = SudokuBoardDaoFactory.getFileDao("D:\\exception.txt");
        Exception exception = assertThrows(FileDaoException.class, () -> {
            SudokuBoard sudokuBoard = sudokuBoardDao.read();
        });
        String expectedMessage = bundle.getString("readF");
        String actualMessage = exception.getLocalizedMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void writeException() {
        Dao<SudokuBoard> sudokuBoardDao = SudokuBoardDaoFactory.getFileDao("/sudoku:");
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        Exception exception = assertThrows(FileDaoException.class, () -> {
            sudokuBoardDao.write(sudokuBoard);
        });
        String expectedMessage = bundle.getString("writeF");
        String actualMessage = exception.getLocalizedMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}