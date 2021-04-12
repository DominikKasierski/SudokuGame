package pl.dk.dao;

import pl.dk.sudoku.SudokuBoard;

public class SudokuBoardDaoFactory {

    public static Dao<SudokuBoard> getFileDao(String fileName) {
        return new FileSudokuBoardDao(fileName);
    }

    public static Dao<SudokuBoard> getJdbcDao(String id) {
        return new JdbcSudokuBoardDao(id);
    }

    private SudokuBoardDaoFactory() {
    }
}
