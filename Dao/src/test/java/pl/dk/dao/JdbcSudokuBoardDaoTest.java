package pl.dk.dao;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dk.dao.exceptions.DaoException;
import pl.dk.dao.exceptions.DatabaseDaoException;
import pl.dk.sudoku.BacktrackingSudokuSolver;
import pl.dk.sudoku.SudokuBoard;

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class JdbcSudokuBoardDaoTest {

    private ResourceBundle bundle = ResourceBundle.getBundle("pl.dk.dao.Language");

    @Test
    void writeAndReadUsingFactory() throws DaoException {
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        sudokuBoard.solveGame();
        Dao<SudokuBoard> jdbcDao = SudokuBoardDaoFactory.getJdbcDao("1");
        jdbcDao.write(sudokuBoard);
        SudokuBoard sudokuBoard1 = jdbcDao.read();
        assertEquals(sudokuBoard, sudokuBoard1);
    }

    @Test
    void readException() {
        Dao<SudokuBoard> jdbcDao = SudokuBoardDaoFactory.getJdbcDao("nieMaTabelki");
        Exception exception = assertThrows(DatabaseDaoException.class, () -> {
            SudokuBoard sudokuBoard = jdbcDao.read();
        });
        String expectedMessage = bundle.getString("readD");
        String actualMessage = exception.getLocalizedMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void writeException() {
        Dao<SudokuBoard> jdbcDao = SudokuBoardDaoFactory.getJdbcDao("przekroczonaDlugoscVarcharaZdecydowanie");
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        Exception exception = assertThrows(DatabaseDaoException.class, () -> {
            jdbcDao.write(sudokuBoard);
        });
        String expectedMessage = bundle.getString("writeD");
        String actualMessage = exception.getLocalizedMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void loggerTest() {
        Logger logger = LoggerFactory.getLogger(JdbcSudokuBoardDaoTest.class);
        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
    }
}