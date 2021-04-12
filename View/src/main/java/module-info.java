module ViewProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires DaoProject;
    requires ModelProject;
    requires slf4j.api;

    opens pl.dk.sudokuView to javafx.fxml;
    exports pl.dk.sudokuView;
    exports pl.dk.sudokuView.exceptions;
}