package pl.dk.sudoku;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dk.sudoku.exceptions.ValueException;


public class BacktrackingSudokuSolver implements SudokuSolver, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(SudokuField.class);

    @Override
    public final void solve(final SudokuBoard board) {
        putRandomNumbersDiagonally(board);
        solveSudoku(board);
    }

    private void putRandomNumbersDiagonally(final SudokuBoard board) {
        List<Integer> randomNumbers = Arrays.asList(new Integer[9]);

        for (int i = 1; i <= board.getRowsNumber(); i++) {
            randomNumbers.set(i - 1, i);
        }

        Collections.shuffle(randomNumbers);

        for (int i = 0; i < board.getRowsNumber(); i++) {
            board.setWithCheck(i, i, randomNumbers.get(i));
        }
    }

    private boolean solveSudoku(final SudokuBoard board) {
        int n = board.getRowsNumber();
        int row = 0;
        int col = 0;
        boolean isEmpty = false;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board.getFieldValue(i, j) == 0) {
                    row = i;
                    col = j;
                    isEmpty = true;
                    break;
                }
            }
            if (isEmpty) {
                break;
            }
        }
        if (!isEmpty) {
            return true;
        }
        for (int num = 1; num <= n; num++) {
            try {
                board.setWithCheck(row, col, num);
                if (solveSudoku(board)) {
                    return true;
                } else {
                    board.setWithCheck(row, col, 0);
                }
            } catch (ValueException e) {
                logger.debug("An error has occurred.", e);
            }
        }
        return false;
    }

    @Override public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
