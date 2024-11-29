package by.shift.minesweeper.service;

import by.shift.minesweeper.model.Game;

public interface GameService {
    Game startNewGame();
    Game revealCell(String id, int row, int col);
    Game toggleFlag(String id, int row, int col);
}