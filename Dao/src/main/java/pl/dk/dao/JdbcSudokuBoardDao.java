package pl.dk.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dk.dao.exceptions.DatabaseDaoException;
import pl.dk.sudoku.BacktrackingSudokuSolver;
import pl.dk.sudoku.SudokuBoard;


public class JdbcSudokuBoardDao implements Dao<SudokuBoard> {

    static final String JDBC_DRIVER = "org.h2.Driver";

    public static String TABLE_NAME = "SUDOKU";

    public static String URL;

    public static String insertData =
            "insert into SUDOKU(ID,fields,isEditable) values (?,?,?)";

    public static String selectData = "select fields, isEditable from "
            + TABLE_NAME + " where ID=?";

    private String id;

    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(JdbcSudokuBoardDao.class);

    public JdbcSudokuBoardDao(String id) {
        this.id = id;
        URL = "jdbc:h2:mem:" + "KOMPO" + ";DB_CLOSE_DELAY=-1";
        try {
            initializeConnection();
        } catch (DatabaseDaoException e) {
            logger.error("An exception occurred", e);
        }
    }

    private void initializeConnection() throws DatabaseDaoException {
        try {
            connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            DatabaseDaoException exception = new DatabaseDaoException("connection");
            exception.initCause(e);
            throw exception;
        }
    }

    @Override public SudokuBoard read() throws DatabaseDaoException {
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        String receivedData;
        ResultSet resultSet;
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectData)) {
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            receivedData = resultSet.getString(1);
            sudokuBoard.stringToValues(receivedData);
            receivedData = resultSet.getString(2);
            sudokuBoard.stringToEditable(receivedData);
        } catch (SQLException e) {
            DatabaseDaoException exception = new DatabaseDaoException("readD");
            exception.initCause(e);
            throw exception;
        }
        return sudokuBoard;
    }

    @Override public void write(SudokuBoard obj) throws DatabaseDaoException {
        try (Statement statement = connection.createStatement()) {

            Class.forName(JDBC_DRIVER);
            String createTable = "create table if not exists " + TABLE_NAME
                    + "(ID varchar(30) primary key not null, "
                    + "fields varchar(81), isEditable varchar(81))";
            statement.execute(createTable);

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertData)) {
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, obj.valuesToString());
                preparedStatement.setString(3, obj.editableToString());
                preparedStatement.executeUpdate();
                connection.commit();
            }

        } catch (SQLException | ClassNotFoundException e) {
            DatabaseDaoException exception = new DatabaseDaoException("writeD");
            exception.initCause(e);
            throw exception;
        }
    }

    @Override public void close() throws DatabaseDaoException {
        try {
            connection.close();
        } catch (SQLException e) {
            DatabaseDaoException exception = new DatabaseDaoException("connectionClose");
            exception.initCause(e);
            throw exception;
        }
    }
}
