package ru.prbank.test_task.seacombat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prbank.test_task.seacombat.domain.exception.CoordinatesException;
import ru.prbank.test_task.seacombat.domain.exception.ShipFormException;
import ru.prbank.test_task.seacombat.domain.exception.ShipsLimitException;
import ru.prbank.test_task.seacombat.domain.model.Deck;
import ru.prbank.test_task.seacombat.domain.model.Game;
import ru.prbank.test_task.seacombat.domain.model.PlayingBoard;
import ru.prbank.test_task.seacombat.domain.model.Ship;

import java.util.List;

/**
 * Реализация сервиса работы с объектами кораблей.
 */
@Service
@AllArgsConstructor
public class ShipServiceImpl implements ShipService {
    /**
     * Реализация метода {@link ShipService#validateShip(Game, PlayingBoard, Ship)}
     * См. описание метода в интерфейсе {@link ShipService}
     *
     * @throws RuntimeException исключения пробрасываются дальше по стеку и обрабатываются контроллером.
     */
    @Override
    public void validateShip(Game game, PlayingBoard boardForPut, Ship ship) throws RuntimeException {
        // Проверка, что игрок не пытается разместить больше кораблей этого типа, чем разрешено
        int size = ship.getDecks().size();
        if (boardForPut.isShipLimitReached(size))
            throw new ShipsLimitException("No more ships of size " + size + " is allowed");

        // Проверка, что корабль не выходит за пределы поля
        if (!ship.isInBounds())
            throw new CoordinatesException("Ship with decks: " + ship.getDecks() + " is out of field bounds");

        // Проверка, что корабль не гнутый
        if (!ship.isStraight()) throw new ShipFormException("Ship with decks: " + ship.getDecks() + " is not straight");

        // Проверка, что корабль цельный
        if (!ship.isSolid()) throw new ShipFormException("Ship with decks: " + ship.getDecks() + " is not solid");

        // Проверка, что корабль не липнет к соседям и не перекрывает их
        if (isOverlap(boardForPut, ship))
            throw new CoordinatesException("Ship with decks: " + ship.getDecks() + " could not be too close to existing " +
                    "ships");
    }

    /**
     * Проверка, что новый корабль не перекрывает поля другого корабля
     * @param boardForPut Игровая доска с умеющимися кораблями
     * @param ship проверяемый корабль
     * @return возвращает true, если корабль перекрывает поля другого корабля
     */
    private boolean isOverlap(PlayingBoard boardForPut, Ship ship) {
        List<Deck> existingDecks = boardForPut.getShips().stream()
                .flatMap(it -> it.getDecks().stream())
                .toList();
        for (Deck deck : ship.getDecks()) {
            if (isAdjacent(deck, existingDecks)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAdjacent(Deck deck, List<Deck> existingDecks) {
        for (Deck existingDeck : existingDecks) {
            if (Math.abs(deck.getCoordinateX() - existingDeck.getCoordinateX()) <= 1
                    && Math.abs(deck.getCoordinateY() - existingDeck.getCoordinateY()) <= 1) {
                return true; // Найдена соседняя клетка, или та же
            }
        }
        return false;
    }
}
