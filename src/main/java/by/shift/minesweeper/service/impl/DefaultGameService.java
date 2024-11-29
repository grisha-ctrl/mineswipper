package by.shift.minesweeper.service.impl;

import by.shift.minesweeper.model.Cell;
import by.shift.minesweeper.model.Game;
import by.shift.minesweeper.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DefaultGameService implements GameService {

    private static final Logger log = LoggerFactory.getLogger(DefaultGameService.class);
    private Game game;

    @Override
    public Game startNewGame() {
        game = new Game("1", 10, 10, 10);
        log.info("Создана игра с фиксированными параметрами");
        return game;
    }

    @Override
    public Game revealCell(String id, int row, int col) {

        log.info("Открываем клетку, строка {} столбец {}", row, col);
        if (game.isGameOver()) {
            return game;
        }

        Cell cell = game.getCell(row, col);

        if (cell.isFlagged()) return game;

        if (!isBoardInitialized()) {
            placeMines(row, col);
            calculateAdjacentMines();
        }

        if (reveal(cell)) {
            game.setGameOver(true);
            log.info("Вы проиграли");
        } else if (cell.getAdjacentMines() == 0) {
            revealAdjacentCells(row, col);
        }
        return game;
    }

    @Override
    public Game toggleFlag(String id, int row, int col) {

        if (game.isGameOver()) {
            return game;
        }

        Cell cell = game.getCell(row, col);

        if (!cell.isRevealed()) {
            if (cell.isFlagged()) {
                log.info("Убирает состояние флажка на указанной клетке, строка {} столбец {}", row, col);
                cell.setFlagged(false);
                game.setFlagCount(game.getFlagCount() + 1);
                if (cell.isMine()) {
                    game.setFlaggedMines(game.getFlaggedMines() - 1);
                }
            } else if (game.getFlagCount() > 0) {
                log.info("Добавляет состояние флажка на указанной клетке, строка {} столбец {}", row, col);
                cell.setFlagged(true);
                game.setFlagCount(game.getFlagCount() - 1);
                if (cell.isMine()) {
                    game.setFlaggedMines(game.getFlaggedMines() + 1);
                }
            }
        }
        checkWinCondition();
        if (game.isGameOver()) {
            log.info("Вы победили");
        }
        return game;
    }

    private void placeMines(int firstRow, int firstCol) {
        Random random = new Random();
        int minesPlaced = 0;

        while (minesPlaced < game.getMinesCount()) {
            int row = random.nextInt(game.getRows());
            int col = random.nextInt(game.getCols());
            if ((Math.abs(row - firstRow) <= 1 && Math.abs(col - firstCol) <= 1) || game.getCell(row, col).isMine()) {
                continue;
            }
            game.getCell(row, col).setMine(true);
            minesPlaced++;
        }
    }

    private void calculateAdjacentMines() {
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getCols(); j++) {
                if (!game.getCell(i, j).isMine()) {
                    game.getCell(i, j).setAdjacentMines(countAdjacentMines(i, j));
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int mineCount = 0;
        int[] directions = {-1, 0, 1};

        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) continue;

                int newRow = row + dx;
                int newCol = col + dy;

                if (newRow >= 0 && newRow < game.getRows() && newCol >= 0 && newCol < game.getCols()) {
                    if (game.getCell(newRow, newCol).isMine()) {
                        mineCount++;
                    }
                }
            }
        }
        return mineCount;
    }

    private boolean isBoardInitialized() {
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getCols(); j++) {
                if (game.getCell(i, j).isMine()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean revealAdjacentCells(int row, int col) {
        boolean mineRevealed = false;
        int[] directions = {-1, 0, 1};

        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) continue;

                int newRow = row + dx;
                int newCol = col + dy;

                if (newRow >= 0 && newRow < game.getRows() && newCol >= 0 && newCol < game.getCols()) {
                    Cell adjacentCell = game.getCell(newRow, newCol);
                    if (!adjacentCell.isRevealed() && !adjacentCell.isFlagged()) {
                        reveal(adjacentCell);
                        if (adjacentCell.isMine()) {
                            mineRevealed = true;
                        } else if (adjacentCell.getAdjacentMines() == 0) {
                            mineRevealed = revealAdjacentCells(newRow, newCol) || mineRevealed;
                        }
                    }
                }
            }
        }
        return mineRevealed;
    }

    private void checkWinCondition() {
        boolean allMinesFlaggedCorrectly = true;

        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getCols(); j++) {
                Cell cell = game.getCell(i, j);

                if (!cell.isFlagged() && cell.isMine()) {
                    allMinesFlaggedCorrectly = false;
                }
            }
        }


        if (allMinesFlaggedCorrectly) {
            game.setGameOver(true);
        }
    }

    public boolean reveal(Cell cell) {
        if (cell.isRevealed() || cell.isFlagged()) {
            return false;
        }
        cell.setRevealed(true);
        return cell.isMine();
    }
}
