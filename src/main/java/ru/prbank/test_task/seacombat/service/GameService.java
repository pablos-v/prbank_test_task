package ru.prbank.test_task.seacombat.service;

import org.springframework.stereotype.Service;
import ru.prbank.test_task.seacombat.domain.enums.ShotResult;
import ru.prbank.test_task.seacombat.domain.model.Game;
import ru.prbank.test_task.seacombat.domain.model.Ship;
import ru.prbank.test_task.seacombat.repository.GameRepository;

import java.util.List;

public interface GameService {


    Long createGame(Long player1, Long player2);

    Game getGame(Long gameId);

    ShotResult shoot(Long gameId, Long playerId, int x, int y);

    void putShip(Long gameId, Long playerId, Ship ship);
}
