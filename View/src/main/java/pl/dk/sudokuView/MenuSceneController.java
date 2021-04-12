package pl.dk.sudokuView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dk.sudoku.DifficultyLevel;
import pl.dk.sudokuView.exceptions.SceneLoadingException;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MenuSceneController {

    private String fxmlFile = "menuScene.fxml";

    @FXML
    private MenuBar menuBar;

    private ResourceBundle bundle = ResourceBundle.getBundle("pl.dk.sudokuView.Language");

    private static final Logger logger = LoggerFactory.getLogger(MenuSceneController.class);

    @FXML
    private RadioButton easyRadioButton, normalRadioButton, hardRadioButton;

    @FXML
    private void changingScene(ActionEvent event) {

        FXMLLoader fxmlLoader =
                new FXMLLoader(MenuSceneController.class.getResource("sudokuScene.fxml"), bundle);

        try {
            Parent parent = fxmlLoader.load();

            SudokuSceneController sudokuSceneController = fxmlLoader.getController();
            if (easyRadioButton.isSelected()) {
                sudokuSceneController.setDifficultyLevel(DifficultyLevel.EASY);
            } else if (normalRadioButton.isSelected()) {
                sudokuSceneController.setDifficultyLevel(DifficultyLevel.NORMAL);
            } else if (hardRadioButton.isSelected()) {
                sudokuSceneController.setDifficultyLevel(DifficultyLevel.HARD);
            }
            logger.info(bundle.getString("difficultyLogger") + " " +
                    sudokuSceneController.getDifficultyLevel());


            Scene scene = new Scene(parent);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);

        } catch (IOException e) {
            SceneLoadingException exception =
                    new SceneLoadingException("sceneException");
            exception.initCause(e);
            logger.error(bundle.getString("logger"), exception);
        }
    }

    @FXML
    private void onActionButtonAngLang(ActionEvent event) {
        Locale locale = new Locale("en", "US");
        if (checkIfLanguageIsNotAlreadyChosen(locale)) {
            Locale.setDefault(locale);
            try {
                SceneReloader.reloadScene(Locale.getDefault(), fxmlFile, menuBar);
                logger.info(bundle.getString("languageLogger") + " " + Locale.getDefault());
            } catch (SceneLoadingException e) {
                logger.error(bundle.getString("logger"), e);
            }
        }
    }

    @FXML
    private void onActionButtonPlLang(ActionEvent event) {
        Locale locale = new Locale("pl", "PL");
        if (checkIfLanguageIsNotAlreadyChosen(locale)) {
            Locale.setDefault(locale);
            try {
                SceneReloader.reloadScene(Locale.getDefault(), fxmlFile, menuBar);
                logger.info(bundle.getString("languageLogger") + " " + Locale.getDefault());
            } catch (SceneLoadingException e) {
                logger.error(bundle.getString("logger"), e);
            }
        }
    }

    @FXML
    private void onActionButtonAuthors(ActionEvent event) {
        ResourceBundle listBundle = ResourceBundle.getBundle("pl.dk.sudokuView.Authors");
        getInfoBox(listBundle);
        logger.info(bundle.getString("authorLogger"));
    }

    private void getInfoBox(ResourceBundle listBundle) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("styles.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("myInfo");
        alert.setTitle(bundle.getString("infoDialog"));
        alert.setHeaderText(null);
        alert.setContentText(listBundle.getString("1. "));
        alert.showAndWait();
    }

    private boolean checkIfLanguageIsNotAlreadyChosen(Locale locale) {
        return !Locale.getDefault().equals(locale);
    }

    public void initialize() {
    }
}
