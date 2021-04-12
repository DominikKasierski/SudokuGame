module ModelProject {
    requires org.apache.commons.lang3;
    requires slf4j.api;
    requires java.desktop;
    opens pl.dk.sudoku;
    exports pl.dk.sudoku;
    exports pl.dk.sudoku.exceptions;
}