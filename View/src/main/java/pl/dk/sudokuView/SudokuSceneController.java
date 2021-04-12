package pl.dk.sudokuView;

import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dk.dao.exceptions.DaoException;
import pl.dk.sudoku.BacktrackingSudokuSolver;
import pl.dk.sudoku.DifficultyLevel;
import pl.dk.sudoku.SudokuBoard;
import pl.dk.dao.Dao;
import pl.dk.dao.SudokuBoardDaoFactory;
import pl.dk.sudokuView.exceptions.BindingException;
import pl.dk.sudokuView.exceptions.SceneLoadingException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SudokuSceneController {

    // background
    static BackgroundFill background_fill = new BackgroundFill(Color.DIMGRAY,
            CornerRadii.EMPTY, Insets.EMPTY);

    static Background dimGray = new Background(background_fill);

    static BackgroundFill background_fill2 = new BackgroundFill(Color.rgb(93, 93, 93),
            CornerRadii.EMPTY, Insets.EMPTY);

    static Background gray = new Background(background_fill2);
    // end of background section

    private static final Logger logger = LoggerFactory.getLogger(SudokuSceneController.class);

    private SudokuBoard sudokuBoard;

    private StringConverter<Number> converter = new NumberStringConverter();

    private List<String> IDs = new ArrayList<>(3);

    private List<JavaBeanIntegerProperty> properties = new ArrayList<>(50);

    private FileChooser fileChooser;

    private File file;

    private Dao<SudokuBoard> fileSudokuBoardDao;

    private Dao<SudokuBoard> jdbcSudokuBoardDao;

    private DifficultyLevel difficultyLevel;

    private String fxmlFile = "sudokuScene.fxml";

    private static final int ROWS_NUMBER = 9;

    private ResourceBundle bundle = ResourceBundle.getBundle("pl.dk.sudokuView.Language");

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setSudokuBoard(SudokuBoard sudokuBoard) {
        this.sudokuBoard = sudokuBoard;
        connectSudokuFieldsWithTextFields();
    }

    @FXML
    private MenuBar menuBar;

    @FXML
    private GridPane sudokuGridPane;

    @FXML
    private Button returnToMenu, save, check, saveDataBase, loadDataBase;

    @FXML
    private void changingScene(ActionEvent event) {
        Stage stage;
        Parent root;
        stage = (Stage) returnToMenu.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("menuScene.fxml"), bundle);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            logger.info(bundle.getString("backToMenuLogger"));
        } catch (IOException e) {
            SceneLoadingException exception =
                    new SceneLoadingException("sceneException");
            exception.initCause(e);
            logger.error(bundle.getString("logger"), exception);
        }
    }

    @FXML
    private void handleNewBoardButtonAction(ActionEvent event) {
        initializeGridPane();
        logger.info(bundle.getString("newBoardLogger"));
    }

    @FXML
    private void handleCheckButtonAction(ActionEvent event) {
        if (!sudokuBoard.ifBoardIsFilled()) {
            AlertBox.display(bundle.getString("notCompletedTitle"),
                    bundle.getString("notCompletedMessage"), bundle.getString("buttonText"));
            logger.info(bundle.getString("checkBoardNotCompletedLogger"));

        } else {
            if (sudokuBoard.checkBoard()) {
                AlertBox.display(bundle.getString("winTitle"), bundle.getString("winMessage"),
                        bundle.getString("buttonText"));
                logger.info(bundle.getString("checkBoardWinLogger"));
            } else {
                AlertBox.display(bundle.getString("loseTitle"), bundle.getString("loseMessage"),
                        bundle.getString("buttonText"));
                logger.info(bundle.getString("checkBoardLoseLogger"));
            }
        }
    }

    private void clearGrid() {
        for (Node node : sudokuGridPane.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).clear();
            }
        }
    }

    private void initializeGridPane() {
        sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        sudokuBoard.solveGame();
        sudokuBoard.prepareInitialSudokuBoard(difficultyLevel);
        connectSudokuFieldsWithTextFields();
    }

    private void connectSudokuFieldsWithTextFields() {
        clearGrid();
        properties.clear();

        int i = 0;
        for (Node node : sudokuGridPane.getChildren()) {
            if (node instanceof TextField) {
                int x = i / ROWS_NUMBER;
                int y = i - (x * ROWS_NUMBER);

                if (!sudokuBoard.getField(x, y).isEditable()) {
                    ((TextField) node).setText(Integer.toString(sudokuBoard.getFieldValue(x, y)));
                    ((TextField) node).setEditable(false);
                    ((TextField) node).setBackground(dimGray);
                } else {
                    ((TextField) node).setEditable(true);
                    ((TextField) node).setBackground(null);
                    ((TextField) node).setTextFormatter(new TextFormatter<>(change ->
                            (change.getControlNewText().matches("([1-9])?")) ? change : null));

                    JavaBeanIntegerPropertyBuilder builder =
                            JavaBeanIntegerPropertyBuilder.create();
                    try {
                        JavaBeanIntegerProperty integerProperty =
                                builder.bean(sudokuBoard.getField(x, y)).name("value")
                                        .getter("getFieldValue").setter("setFieldValue").build();
                        ((TextField) node).textProperty()
                                .bindBidirectional(integerProperty, converter);
                        properties.add(integerProperty);
                    } catch (NoSuchMethodException e) {
                        BindingException exception =
                                new BindingException("bindingException");
                        exception.initCause(e);
                        logger.error(bundle.getString("logger"), exception);
                    }
                }
                i++;
            }
        }
        switchOnSaveAndCheckButton();
    }

    private void switchOnSaveAndCheckButton() {
        if (save.isDisable()) {
            save.setDisable(false);
        }
        if (saveDataBase.isDisable()) {
            saveDataBase.setDisable(false);
        }
        if (check.isDisable()) {
            check.setDisable(false);
        }
    }

    @FXML
    private void onActionButtonAngLangSudoku(ActionEvent event) {
        Locale locale = new Locale("en", "US");

        if (checkIfLanguageIsNotAlreadyChosen(locale)) {
            Alert alert = getAlertBox();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    Locale.setDefault(locale);
                    try {
                        SceneReloader
                                .reloadScene(Locale.getDefault(), fxmlFile, menuBar,
                                        difficultyLevel);
                        logger.info(bundle.getString("languageLogger") + " " + Locale.getDefault());

                    } catch (SceneLoadingException e) {
                        logger.error(bundle.getString("logger"), e);
                    }
                }
            }
        }
    }

    @FXML
    private void onActionButtonPlLangSudoku(ActionEvent event) {
        Locale locale = new Locale("pl", "PL");

        if (checkIfLanguageIsNotAlreadyChosen(locale)) {
            Alert alert = getAlertBox();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    Locale.setDefault(locale);
                    try {
                        SceneReloader
                                .reloadScene(Locale.getDefault(), fxmlFile, menuBar,
                                        difficultyLevel);
                        logger.info(bundle.getString("languageLogger") + " " + Locale.getDefault());
                    } catch (SceneLoadingException e) {
                        logger.error(bundle.getString("logger"), e);
                    }

                }
            }
        }
    }

    private boolean checkIfLanguageIsNotAlreadyChosen(Locale locale) {
        return !Locale.getDefault().equals(locale);
    }

    @FXML
    private void onActionButtonSave(ActionEvent event) {
        fileChooser = new FileChooser();
        file = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        try {
            fileSudokuBoardDao = SudokuBoardDaoFactory.getFileDao(file.getAbsolutePath());
            fileSudokuBoardDao.write(sudokuBoard);
            getInfoBox("saveInfo");
            logger.info(bundle.getString("saveFileLogger"));
        } catch (DaoException e) {
            logger.error(bundle.getString("logger"), e);
            getInfoBox("saveNegativeInfo");
        } catch (NullPointerException e) {
            logger.debug(bundle.getString("logger"), e);
        }
    }

    @FXML
    private void onActionButtonDatabaseSave(ActionEvent event) {
        String result = getTextFromInputDialog();
        if (result != null && !result.equals("")) {
            IDs.add(result);
            try {
                jdbcSudokuBoardDao = SudokuBoardDaoFactory.getJdbcDao(result);
                jdbcSudokuBoardDao.write(sudokuBoard);
                switchOnLoadButton();
                getInfoBox("saveInfo");
                logger.info(bundle.getString("saveDatabaseLogger"));
            } catch (DaoException e) {
                logger.error(bundle.getString("logger"), e);
                getInfoBox("saveNegativeInfo");
            }
        }
    }

    @FXML
    private void onActionButtonLoad(ActionEvent event) {
        fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        try {
            fileSudokuBoardDao = SudokuBoardDaoFactory.getFileDao(file.getAbsolutePath());
            setSudokuBoard(fileSudokuBoardDao.read());
            getInfoBox("loadInfo");
            logger.info(bundle.getString("loadFileLogger"));
        } catch (DaoException e) {
            logger.error(bundle.getString("logger"), e);
            getInfoBox("loadNegativeInfo");
        } catch (NullPointerException e) {
            logger.debug(bundle.getString("logger"), e);
        }
    }

    @FXML
    private void onActionButtonDatabaseLoad(ActionEvent event) {
        String result = getRecordIDFromChoiceDialog();
        if (result != null) {
            jdbcSudokuBoardDao = SudokuBoardDaoFactory.getJdbcDao(result);
            try {
                setSudokuBoard(jdbcSudokuBoardDao.read());
                getInfoBox("loadInfo");
                logger.info(bundle.getString("loadDatabaseLogger"));
            } catch (DaoException e) {
                logger.error(bundle.getString("logger"), e);
                getInfoBox("loadNegativeInfo");
            }
        }
    }

    private void switchOnLoadButton() {
        if (loadDataBase.isDisable()) {
            loadDataBase.setDisable(false);
        }
    }

    @FXML
    private void onActionButtonAuthors(ActionEvent event) {
        ResourceBundle listBundle = ResourceBundle.getBundle("pl.dk.sudokuView.Authors");
        getInfoBoxAuthors(listBundle);
        logger.info(bundle.getString("authorLogger"));
    }

    private void getInfoBoxAuthors(ResourceBundle listBundle) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("styles.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("myInfo");
        alert.setTitle(bundle.getString("infoDialog"));
        alert.setHeaderText(null);
        alert.setContentText(listBundle.getString("1. "));
        alert.showAndWait();
    }

    private void getInfoBox(String key) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("styles.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("myInfo");
        alert.setTitle(bundle.getString("confirmTitle"));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString(key));
        alert.showAndWait();
    }

    private String getTextFromInputDialog() {
        String ID = null;
        TextInputDialog dialog = new TextInputDialog();
        dialog.getDialogPane().getStylesheets()
                .add(getClass().getResource("styles.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("myTextDialog");
        dialog.setTitle(bundle.getString("textDialog"));
        dialog.setHeaderText(null);
        dialog.setContentText(bundle.getString("contentTextDialog"));

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            ID = result.get();
        }
        return ID;
    }

    private String getRecordIDFromChoiceDialog() {
        String ID = null;
        ChoiceDialog<String> dialog = new ChoiceDialog<>(IDs.get(0), IDs);
        dialog.getDialogPane().getStylesheets()
                .add(getClass().getResource("styles.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("myTextDialog");
        dialog.setTitle(bundle.getString("choiceDialog"));
        dialog.setHeaderText(null);
        dialog.setContentText(bundle.getString("choiceText"));

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            ID = result.get();
        }
        return ID;
    }


    private Alert getAlertBox() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("styles.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("myDialog");
        alert.setTitle(bundle.getString("confirmTitle"));
        alert.setHeaderText(bundle.getString("headerText"));
        alert.setContentText(bundle.getString("contentText"));
        alert.getDialogPane().getButtonTypes().clear();
        alert.getDialogPane().getButtonTypes().add(
                new ButtonType(bundle.getString("ok"), ButtonBar.ButtonData.OK_DONE));
        alert.getDialogPane().getButtonTypes().add(
                new ButtonType(bundle.getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE));
        return alert;
    }

    public void initialize() {
        save.setDisable(true);
        for (Node node : sudokuGridPane.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).setBackground(dimGray);
            }
        }
    }
}