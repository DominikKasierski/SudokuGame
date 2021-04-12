package pl.dk.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import pl.dk.dao.exceptions.FileDaoException;
import pl.dk.sudoku.SudokuBoard;


public class FileSudokuBoardDao implements Dao<SudokuBoard> {

    private String fileName;

    public FileSudokuBoardDao(String fileName) {
        this.fileName = fileName;
    }

    /**
     * This method save sudokuBoard to file.
     *
     * @param obj is a sudokuBoard which we want to save.
     */
    public void write(SudokuBoard obj) throws FileDaoException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(
                new FileOutputStream(fileName))) {
            outputStream.writeObject(obj);
        } catch (IOException e) {
            FileDaoException exception = new FileDaoException("writeF");
            exception.initCause(e);
            throw exception;
        }
    }

    /**
     * This method load sudokuBoard from file.
     *
     * @return loaded sudokuBoard.
     */
    public SudokuBoard read() throws FileDaoException {
        SudokuBoard deserializedBoard = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            deserializedBoard = (SudokuBoard) inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            FileDaoException exception = new FileDaoException("readF");
            exception.initCause(e);
            throw exception;
        }
        return deserializedBoard;
    }

    @Override public void close() throws Exception {

    }
}

