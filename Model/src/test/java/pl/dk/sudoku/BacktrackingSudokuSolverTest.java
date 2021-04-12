package pl.dk.sudoku;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BacktrackingSudokuSolverTest {

    private static final Logger logger =
            LoggerFactory.getLogger(BacktrackingSudokuSolverTest.class);

    private boolean checkSquares(SudokuBoard sudoku) {
        int sqrt = 3;
        int boxRowStart = 0;
        int boxColStart = 0;
        int counter = 0;
        for (int g = 0; g < 9; g++) {
            Integer[] check = new Integer[9];
            int j = 0;
            if (g <= 2) {
                boxRowStart = 0;
            }
            if (g > 2 && g <= 5) {
                boxRowStart = 3;
            }
            if (g > 5) {
                boxRowStart = 6;
            }

            for (int r = boxRowStart; r < boxRowStart + sqrt; r++) {
                for (int d = boxColStart; d < boxColStart + sqrt; d++) {
                    check[j] = sudoku.getBoard()[r * 9 + d];
                    j++;
                }
            }

            Set<Integer> s = new HashSet<>(Arrays.asList(check));

            if (s.size() == check.length) {
                counter++;
            }

            boxColStart += 3;

            if (boxColStart == 9) {
                boxColStart = 0;
            }
        }
        return counter == 9;
    }

    private boolean checkCols(SudokuBoard sudoku) {
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            Integer[] check = new Integer[9];
            for (int j = 0; j < 9; j++) {
                check[j] = sudoku.getBoard()[i + j * 9];
            }

            Set<Integer> s = new HashSet<>(Arrays.asList(check));

            if (s.size() == check.length) {
                counter++;
            }
        }
        return counter == 9;
    }

    private boolean checkingRows(SudokuBoard sudoku) {
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            Integer[] check = new Integer[9];
            for (int j = 0; j < 9; j++) {
                check[j] = sudoku.getBoard()[i * 9 + j];
            }

            Set<Integer> s = new HashSet<>(Arrays.asList(check));

            if (s.size() == check.length) {
                counter++;
            }
        }
        return counter == 9;
    }

    @Test
    public void ifSolveGeneratesCorrectSudokuBoard() {
        SudokuBoard sudoku = new SudokuBoard(new BacktrackingSudokuSolver());

        sudoku.solveGame();
        assertTrue(checkingRows(sudoku));
        assertTrue(checkCols(sudoku));
        assertTrue(checkSquares(sudoku));
    }

    @Test
    public void ifSolveGeneratesDifferentBoardsEachTime() {
        SudokuBoard sudoku1 = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuBoard sudoku2 = new SudokuBoard(new BacktrackingSudokuSolver());

        int[] tab1 = new int[81];
        int[] tab2 = new int[81];

        sudoku1.solveGame();
        tab1 = sudoku1.getBoard();
        sudoku2.solveGame();
        tab2 = sudoku2.getBoard();

        assertFalse(Arrays.equals(tab1, tab2));
    }

    @Test
    void toStringTest() {
        BacktrackingSudokuSolver backtrackingSudokuSolver = new BacktrackingSudokuSolver();
        logger.debug(backtrackingSudokuSolver.toString());
    }

}