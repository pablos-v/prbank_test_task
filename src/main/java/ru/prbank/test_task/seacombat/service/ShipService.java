package ru.prbank.test_task.seacombat.service;

import ru.prbank.test_task.seacombat.domain.model.Game;
import ru.prbank.test_task.seacombat.domain.model.PlayingBoard;
import ru.prbank.test_task.seacombat.domain.model.Ship;
import ru.prbank.test_task.seacombat.repository.ShipRepository;

public interface ShipService {
    void validateShip(Game game, PlayingBoard boardForPut, Ship ship);
}
