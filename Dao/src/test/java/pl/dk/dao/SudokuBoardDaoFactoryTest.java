package pl.dk.dao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SudokuBoardDaoFactoryTest {

    @Test
    void getFileDaoTest() {
        assertTrue(SudokuBoardDaoFactory.getFileDao("test.txt") instanceof FileSudokuBoardDao);
    }

    @Test
    void getJdbcDaoTest() {
        assertTrue(SudokuBoardDaoFactory.getJdbcDao("test.txt") instanceof JdbcSudokuBoardDao);
    }


}