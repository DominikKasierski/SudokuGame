package pl.dk.sudokuView;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    public static void display(String title, String message, String button) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setResizable(false);
        window.setMinWidth(250);
        window.setMinHeight(200);

        Label label = new Label();
        label.setText(message);

        //css
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 17));
        label.setTextFill(Paint.valueOf("white"));
        //css

        Button closeButton = new Button();
        closeButton.setText(button);

        //css
        closeButton.setBackground(SudokuSceneController.gray);
        closeButton.setTextFill(Paint.valueOf("white"));
        closeButton.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        closeButton.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        //css

        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(20);
        Region region = new Region();
        region.setMinSize(350, 5);
        layout.getChildren().addAll(label, region, closeButton);

        //css
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(SudokuSceneController.dimGray);
        //css

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}
