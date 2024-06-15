package ru.prbank.test_task.seacombat.service;

import ru.prbank.test_task.seacombat.domain.exception.CoordinatesException;
import ru.prbank.test_task.seacombat.domain.exception.ShipFormException;
import ru.prbank.test_task.seacombat.domain.exception.ShipsLimitException;
import ru.prbank.test_task.seacombat.domain.model.PlayingBoard;
import ru.prbank.test_task.seacombat.domain.model.Ship;

/**
 * Интерфейс взаимодействия с кораблями.
 */
public interface ShipService {
    /**
     * Валидация корабля по расположению, форме, целостности и количеству кораблей такого типа на поле.
     *
     * @param boardForPut Игровая доска с имеющимися кораблями.
     * @param ship        Объект проверяемого корабля.
     * @throws CoordinatesException если корабль перекрывает поля другого корабля или выходит за границы игровой доски.
     * @throws ShipFormException    если форма корабля не прямая, или он не цельный.
     * @throws ShipsLimitException  если количество кораблей больше максимального.
     */
    void validateShip(PlayingBoard boardForPut, Ship ship) throws CoordinatesException, ShipFormException,
            ShipsLimitException;

}
