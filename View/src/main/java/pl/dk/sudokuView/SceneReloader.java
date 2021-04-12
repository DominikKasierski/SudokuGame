package pl.dk.sudokuView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import pl.dk.sudoku.DifficultyLevel;
import pl.dk.sudokuView.exceptions.SceneLoadingException;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SceneReloader {
    public static void reloadScene(Locale locale, String fxmlFile, MenuBar menu)
            throws SceneLoadingException {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(MenuSceneController.class.getResource(fxmlFile), ResourceBundle
                            .getBundle("pl.dk.sudokuView.Language", locale));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = (Stage) menu.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            SceneLoadingException exception =
                    new SceneLoadingException("sceneException");
            exception.initCause(e);
            throw exception;
        }
    }

    public static void reloadScene(Locale locale, String fxmlFile, MenuBar menu,
                                   DifficultyLevel difficultyLevel) throws SceneLoadingException {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(MenuSceneController.class.getResource(fxmlFile), ResourceBundle
                            .getBundle("pl.dk.sudokuView.Language", locale));
            Parent parent = fxmlLoader.load();
            SudokuSceneController sudokuSceneController = fxmlLoader.getController();
            sudokuSceneController.setDifficultyLevel(difficultyLevel);
            Scene scene = new Scene(parent);
            Stage stage = (Stage) menu.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            SceneLoadingException exception =
                    new SceneLoadingException("sceneException");
            exception.initCause(e);
            throw exception;
        }
    }

}
