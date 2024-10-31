package by.shift.minesweeper.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import by.shift.minesweeper.model.Game;

@RestController
@RequestMapping("/api")
public class GameController {

    @PostMapping("/game")
    public Game startNewGame() {
        return null;
    }

    @PostMapping("/game/{id}/reveal")
    public Game revealCell(@PathVariable("id") String id, @RequestParam int row, @RequestParam int col) {
        return null;
    }

    @PostMapping("/game/{id}/toggle-flag")
    public Game toggleFlag(@PathVariable("id") String id, @RequestParam int row, @RequestParam int col) {
        return null;
    }
}
