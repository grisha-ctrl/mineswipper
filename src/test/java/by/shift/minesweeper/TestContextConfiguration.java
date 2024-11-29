package by.shift.minesweeper;

import by.shift.minesweeper.service.GameService;
import by.shift.minesweeper.service.impl.DefaultGameService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestContextConfiguration {
    @Bean
    public GameService gameService() {
        return new DefaultGameService();
    }
}
