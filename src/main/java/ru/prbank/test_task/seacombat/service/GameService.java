package ru.prbank.test_task.seacombat.service;

import org.springframework.stereotype.Service;
import ru.prbank.test_task.seacombat.domain.enums.ShotResult;
import ru.prbank.test_task.seacombat.domain.model.Game;
import ru.prbank.test_task.seacombat.repository.GameRepository;

import java.util.List;

public interface GameService {


    Long createGame(String player1Name, String player2Name);

    Game getGame(Long gameId);

    ShotResult shoot(Long gameId, Long playerId, int x, int y);
}
