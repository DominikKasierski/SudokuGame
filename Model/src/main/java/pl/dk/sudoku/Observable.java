package pl.dk.sudoku;


public interface Observable {
    void attach(Observer observer);

    void detach(Observer observer);

    void notifyObservers(int value);
}
