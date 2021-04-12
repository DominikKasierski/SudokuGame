package pl.dk.sudoku;

public enum DifficultyLevel {
    EASY(35), NORMAL(45), HARD(55);
    private int numberOfFieldsToRemove;

    DifficultyLevel(int numberOfFieldsToRemove) {
        this.numberOfFieldsToRemove = numberOfFieldsToRemove;
    }

    public int getNumberOfFieldsToRemove() {
        return numberOfFieldsToRemove;
    }



}
