package by.shift.minesweeper.model;


public class Game {
    private final String id;
    private final int rows;
    private final int cols;
    private final int minesCount;
    private int flagCount;
    private int flaggedMines;
    private Cell[][] board;
    private boolean gameOver;

    public Game(String id, int rows, int cols, int minesCount) {
        this.id = id;
        this.rows = rows;
        this.cols = cols;
        this.minesCount = minesCount;
        this.board = new Cell[rows][cols];
        this.gameOver = false;
        this.flaggedMines = 0;
        this.flagCount = minesCount;

        initializeBoard();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] mockBoard) {
        this.board = mockBoard;
    }

    public Cell getCell(int row, int col) {
        return board[row][col];
    }

    public void setCell(int row, int col, Cell cell) {
        board[row][col] = cell;
    }

    public int getFlaggedMines() {
        return flaggedMines;
    }

    public void setFlaggedMines(int flaggedMines) {
        this.flaggedMines = flaggedMines;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getMinesCount() {
        return minesCount;
    }

    public int getFlagCount() {
        return flagCount;
    }

    public void setFlagCount(int flagCount) {
        this.flagCount = flagCount;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    private void initializeBoard() {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                setCell(i, j, new Cell());
            }
        }
    }
}


