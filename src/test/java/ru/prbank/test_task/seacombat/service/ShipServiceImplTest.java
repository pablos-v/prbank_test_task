package ru.prbank.test_task.seacombat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.prbank.test_task.seacombat.domain.exception.CoordinatesException;
import ru.prbank.test_task.seacombat.domain.exception.ShipFormException;
import ru.prbank.test_task.seacombat.domain.exception.ShipsLimitException;
import ru.prbank.test_task.seacombat.domain.model.Deck;
import ru.prbank.test_task.seacombat.domain.model.Game;
import ru.prbank.test_task.seacombat.domain.model.PlayingBoard;
import ru.prbank.test_task.seacombat.domain.model.Ship;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipServiceImplTest {
    Game game;
    PlayingBoard board;
    ShipServiceImpl shipService;

    @BeforeEach
    void init() {
        game = new Game(1L, 2L);
        board = new PlayingBoard(1L);
        shipService = new ShipServiceImpl();
        board.setShips(new ArrayList<>());
    }

    @Test
    public void validateShipSuccess() {
        Ship ship = new Ship(List.of(new Deck(1, 1), new Deck(1, 2), new Deck(1, 3)));

        assertDoesNotThrow(() -> shipService.validateShip(game, board, ship));
    }

    @Test
    public void validateShipThrowsShipsLimitException() {
        Ship oldShip = new Ship(List.of(new Deck(1, 1), new Deck(1, 2), new Deck(1, 3), new Deck(1, 4)));
        Ship newShip = new Ship(List.of(new Deck(10, 1), new Deck(10, 2), new Deck(10, 3), new Deck(10, 4)));
        board.setShips(List.of(oldShip));

        ShipsLimitException exception = assertThrows(ShipsLimitException.class,
                () -> shipService.validateShip(game, board, newShip));

        assertEquals("No more ships of size 4 is allowed", exception.getMessage());
    }

    @Test
    public void validateShipThrowsShipFormExceptionForNotStraight() {
        Ship notStraightShip = new Ship(List.of(new Deck(1, 1), new Deck(1, 2), new Deck(2, 2)));

        ShipFormException exception = assertThrows(ShipFormException.class,
                () -> shipService.validateShip(game, board, notStraightShip));

        assertEquals("Ship with decks: " + notStraightShip.getDecks() + " is not straight", exception.getMessage());
    }

    @Test
    public void validateShipThrowsShipFormExceptionForNotSolid() {
        Ship notSolidShip = new Ship(List.of(new Deck(1, 1), new Deck(1, 2), new Deck(1, 8)));

        ShipFormException exception = assertThrows(ShipFormException.class,
                () -> shipService.validateShip(game, board, notSolidShip));

        assertEquals("Ship with decks: " + notSolidShip.getDecks() + " is not solid", exception.getMessage());
    }

    @Test
    public void validateShipThrowsCoordinatesException() {
        Ship ship = new Ship(List.of(new Deck(21, -3)));

        CoordinatesException exception = assertThrows(CoordinatesException.class,
                () -> shipService.validateShip(game, board, ship));

        assertEquals("Ship with decks: " + ship.getDecks() + " is out of field bounds", exception.getMessage());
    }

    @Test
    public void validateShipThrowsCoordinatesExceptionForOverlap()  {
        Ship oldShip = new Ship(List.of(new Deck(10, 1), new Deck(10, 2)));
        Ship newShip = new Ship(List.of(new Deck(10, 1), new Deck(10, 2)));
        board.setShips(List.of(oldShip));

        CoordinatesException exception = assertThrows(CoordinatesException.class,
                () -> shipService.validateShip(game, board, newShip));

        assertEquals("Ship with decks: " + oldShip.getDecks() + " could not be too close to existing ships", exception.getMessage());
    }
}