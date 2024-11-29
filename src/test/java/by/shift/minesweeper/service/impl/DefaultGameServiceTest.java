package by.shift.minesweeper.service.impl;

import by.shift.minesweeper.TestContextConfiguration;
import by.shift.minesweeper.model.Cell;
import by.shift.minesweeper.model.Game;
import by.shift.minesweeper.service.GameService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestContextConfiguration.class)
public class DefaultGameServiceTest {

    @Autowired
    private GameService gameService;
    private final Cell[][] mockBoard = new Cell[10][10];

    @Test
    void startGameTest() {
        Game expected = new Game("1", 10, 10, 10);
        Game actual = gameService.startNewGame();
        Assertions.assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    void revealCellTest() {
        gameService.startNewGame();
        Game game;
        game = gameService.revealCell("1", 0, 0);
        Cell cell = game.getCell(0, 0);
        Assertions.assertThat(cell.isRevealed()).isEqualTo(true);
        Assertions.assertThat(cell.isFlagged()).isEqualTo(false);
        Assertions.assertThat(cell.isMine()).isEqualTo(false);
    }

    @Test
    void toggleFlagTest() {
        fillMockedBoard();
        Game game;
        gameService.startNewGame();
        game = gameService.revealCell("1", 0, 0);
        game.setBoard(mockBoard);
        Game actual = gameService.revealCell("1", 0, 0);
        gameService.toggleFlag("1", 0, 3);
        Cell cell = actual.getCell(0, 3);
        Assertions.assertThat(cell.isRevealed()).isEqualTo(false);
        Assertions.assertThat(cell.isFlagged()).isEqualTo(true);
        gameService.toggleFlag("1", 0, 3);
        Assertions.assertThat(cell.isRevealed()).isEqualTo(false);
        Assertions.assertThat(cell.isFlagged()).isEqualTo(false);
    }

    @Test
    void revealCellWhenGameIsOverTest() {
        Game game;
        gameService.startNewGame();
        game = gameService.revealCell("1", 0, 0);
        game.setGameOver(true);
        Assertions.assertThat(game).isEqualTo(gameService.revealCell("1", 2, 2));
    }

    @Test
    void revealCellWhenCellIsMineTest() {
        fillMockedBoard();
        Game game;
        gameService.startNewGame();
        game = gameService.revealCell("1", 0, 0);
        game.setBoard(mockBoard);
        Game actual = gameService.revealCell("1", 0, 3);
        Assertions.assertThat(actual.isGameOver()).isEqualTo(true);
    }

    @Test
    void openCellAndRevealedAdjacentCellsTest() {
        fillMockedBoard();
        Game game;
        gameService.startNewGame();
        game = gameService.revealCell("1", 0, 0);
        game.setBoard(mockBoard);
        Game actual = gameService.revealCell("1", 0, 0);
        List<Cell> revealedCells = new ArrayList<>();
        revealedCells.add(actual.getCell(0, 0));
        revealedCells.add(actual.getCell(0, 1));
        revealedCells.add(actual.getCell(0, 2));
        revealedCells.add(actual.getCell(1, 0));
        revealedCells.add(actual.getCell(1, 1));
        revealedCells.add(actual.getCell(1, 2));
        revealedCells.add(actual.getCell(2, 0));
        revealedCells.add(actual.getCell(2, 1));
        Cell[][] actualBoard = actual.getBoard();
        for (int row = 0; row < actual.getRows(); row++) {
            for (int col = 0; col < actual.getCols(); col++) {
                Assertions.assertThat(actualBoard[row][col].isRevealed())
                        .isEqualTo(revealedCells.contains(actualBoard[row][col]));
            }
        }
    }

    @Test
    void allMinesFlaggedTest() {
        fillMockedBoard();
        Game game;
        gameService.startNewGame();
        game = gameService.revealCell("1", 0, 0);
        game.setBoard(mockBoard);
        gameService.revealCell("1", 0, 0);
        gameService.toggleFlag("1", 0, 3);
        gameService.toggleFlag("1", 1, 9);
        gameService.toggleFlag("1", 2, 2);
        gameService.toggleFlag("1", 3, 0);
        gameService.toggleFlag("1", 4, 3);
        gameService.toggleFlag("1", 4, 7);
        gameService.toggleFlag("1", 6, 2);
        gameService.toggleFlag("1", 6, 7);
        gameService.toggleFlag("1", 7, 5);
        Game actual = gameService.toggleFlag("1", 9, 5);

        Assertions.assertThat(actual.isGameOver()).isEqualTo(true);
    }

    private void fillMockedBoard() {
        String[] boardData = {
                "001*100011",
                "012210001*",
                "12*1000011",
                "*222101110",
                "111*101*10",
                "0122102220",
                "01*1112*10",
                "01111*2110",
                "0000222000",
                "00001*1000"
        };
        for (int i = 0; i < boardData.length; i++) {
            for (int j = 0; j < boardData[i].length(); j++) {

                char cell = boardData[i].charAt(j);
                if (cell == '*') {
                    mockBoard[i][j] = new Cell();
                    mockBoard[i][j].setMine(true);
                    mockBoard[i][j].setRevealed(false);
                    mockBoard[i][j].setFlagged(false);
                    mockBoard[i][j].setAdjacentMines(-1);
                } else {
                    int value = Character.getNumericValue(cell);
                    mockBoard[i][j] = new Cell();
                    mockBoard[i][j].setMine(false);
                    mockBoard[i][j].setRevealed(false);
                    mockBoard[i][j].setFlagged(false);
                    mockBoard[i][j].setAdjacentMines(value);
                }
            }
        }
    }
}
