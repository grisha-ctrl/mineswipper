package by.shift.minesweeper.controller;

import by.shift.minesweeper.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import by.shift.minesweeper.model.Game;

@Slf4j
@RestController
@RequestMapping("/api")
public class GameController {
    @Autowired
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/game")
    public Game startNewGame() {
        return gameService.startNewGame();
    }

    @PostMapping("/game/{id}/reveal")
    public Game revealCell(@PathVariable String id, @RequestParam int row, @RequestParam int col) {
        return gameService.revealCell(id, row, col);
    }

    @PostMapping("/game/{id}/toggle-flag")
    public Game toggleFlag(@PathVariable("id") String id, @RequestParam int row, @RequestParam int col) {
        return gameService.toggleFlag(id, row, col);
    }
}
