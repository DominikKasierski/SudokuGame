package pl.dk.sudokuView;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dk.sudokuView.exceptions.SceneLoadingException;

import java.io.IOException;
import java.util.ResourceBundle;


public class MainApp extends Application {

    private ResourceBundle bundle = ResourceBundle.getBundle("pl.dk.sudokuView.Language");
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    @Override
    public void start(Stage stage) throws SceneLoadingException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("menuScene.fxml"), bundle);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            stage.setTitle("Sudoku");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.error(bundle.getString("logger"), e);
            SceneLoadingException exception = new SceneLoadingException("sceneException");
            exception.initCause(e);
            throw exception;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
