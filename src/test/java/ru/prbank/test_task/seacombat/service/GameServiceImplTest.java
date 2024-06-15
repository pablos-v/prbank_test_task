package ru.prbank.test_task.seacombat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.prbank.test_task.seacombat.domain.enums.ShotResult;
import ru.prbank.test_task.seacombat.domain.exception.*;
import ru.prbank.test_task.seacombat.domain.model.Deck;
import ru.prbank.test_task.seacombat.domain.model.Game;
import ru.prbank.test_task.seacombat.domain.model.Ship;
import ru.prbank.test_task.seacombat.repository.GameRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceImplTest {
    GameRepository gameRepository;
    PlayerService playerService;
    ShipService shipService;
    GameServiceImpl gameService;
    Long player1;
    Long player2;
    Game game;
    Ship ship1;
    Ship ship2;
    int x;
    int y;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        playerService = mock(PlayerService.class);
        shipService = new ShipServiceImpl();
        gameService = new GameServiceImpl(gameRepository, playerService, shipService);
        player1 = 1L;
        player2 = 2L;
        game = new Game(player1, player2);
        game.getBoards().get(0).setShips(new ArrayList<>());
        game.getBoards().get(1).setShips(new ArrayList<>());
        ship1 = new Ship(List.of(new Deck(5, 5)));
        ship2 = new Ship(List.of(new Deck(2, 1), new Deck(2, 2)));
        x = 5;
        y = 5;
    }

    @Test
    public void getGameSuccess() {
        Long gameId = 1L;
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        Game result = gameService.getGame(gameId);

        assertNotNull(result);
        assertEquals(game, result);
        assertEquals(2, result.getBoards().size());
        assertEquals(1L, result.getBoards().get(0).getOwnerId());
        assertEquals(2L, result.getBoards().get(1).getOwnerId());
    }

    @Test
    public void getGameThrowsGameNotFoundException() {
        Long gameId = -11L;
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        GameNotFoundException exc = assertThrows(GameNotFoundException.class, () -> gameService.getGame(gameId));

        assertEquals("Game with ID -11 not found", exc.getMessage());
    }

    @Test
    public void createGameSuccess() {
        game.setId(17L);

        when(playerService.isPlayerExists(player1)).thenReturn(true);
        when(playerService.isPlayerExists(player2)).thenReturn(true);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Long gameId = gameService.createGame(player1, player2);

        assertNotNull(gameId);
        assertEquals(17, gameId);
    }

    @Test
    public void createGameThrowsPlayerNotFoundException() {
        player2 = -3L;

        when(playerService.isPlayerExists(player1)).thenReturn(true);
        when(playerService.isPlayerExists(player2)).thenReturn(false);

        PlayerNotFoundException exc = assertThrows(PlayerNotFoundException.class,
                () -> gameService.createGame(player1, player2));
        assertEquals("Player with ID -3 not found", exc.getMessage());
    }

    @Test
    public void putShipSuccess() {
        Long gameId = 1L;
        game.setId(gameId);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        gameService.putShip(gameId, player1, 5, 5, 5, 5);
        gameService.putShip(gameId, player2, 2, 1, 2, 2);

        assertTrue(game.getBoards().get(0).getShips().contains(ship1));
        assertTrue(game.getBoards().get(1).getShips().contains(ship2));
        verify(gameRepository, times(2)).save(game);
    }

    @Test
    public void putShipIfGameOverThrowsGameNotFoundException()  {
        Long gameId = 20L;
        game.setId(gameId);
        game.setWinnerId(player1);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        GameNotFoundException exc = assertThrows(GameNotFoundException.class,
                () -> gameService.putShip(gameId, player1, 5, 5, 5, 5));
        assertEquals("Game with ID 20 is over", exc.getMessage());
    }

    @Test
    public void putShipThrowsGameNotFoundException() {
        Long gameId = 999L;

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        GameNotFoundException exc = assertThrows(GameNotFoundException.class,
                () -> gameService.putShip(gameId, player1, 5, 5, 5, 5));
        assertEquals("Game with ID 999 not found", exc.getMessage());
    }

    @Test
    public void putShipThrowsPlayerNotFoundException() {
        Long gameId = 55L;
        player2 = 3L;
        game.setId(gameId);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        PlayerNotFoundException exc = assertThrows(PlayerNotFoundException.class,
                () -> gameService.putShip(gameId, player2, 5, 5, 5, 5));
        assertEquals("Player with ID 3 not found in game with ID 55", exc.getMessage());
    }

    @Test
    public void putShipBoundsCoordinatesException() {
        Long gameId = 1L;
        ship1 = new Ship(List.of(new Deck(11, 5), new Deck(11, 6)));

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        CoordinatesException exc = assertThrows(CoordinatesException.class,
                () -> gameService.putShip(gameId, player1, 11, 5, 11, 6));
        assertEquals("Ship with decks: " + ship1.getDecks() + " is out of field bounds", exc.getMessage());
    }

    @Test
    public void putShipNotAtLineCoordinatesException()  {
        Long gameId = 1L;
        ship1 = new Ship(List.of(new Deck(1, 1), new Deck(1, 2), new Deck(2, 2)));

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        CoordinatesException exc = assertThrows(CoordinatesException.class,
                () -> gameService.putShip(gameId, player1, 1, 1, 2, 2));
        assertEquals("Ship coordinates must be at one line",
                exc.getMessage());
    }

    @Test
    public void putShipOverlapCoordinatesException()  {
        Long gameId = 1L;
        ship1 = new Ship(List.of(new Deck(1, 1), new Deck(1, 2)));
        ship2 = new Ship(List.of(new Deck(1, 2), new Deck(2, 2)));

        game.getBoards().get(0).getShips().add(ship2);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        CoordinatesException exc = assertThrows(CoordinatesException.class,
                () -> gameService.putShip(gameId, player1, 1, 1, 1, 2));
        assertEquals("Ship with decks: " + ship1.getDecks() + " could not be too close to existing ships",
                exc.getMessage());
    }

    @Test
    public void shootSuccessHit() {
        Long gameId = 1L;
        x = 2;
        y = 2;
        ship2.getDecks().get(1).setId(1L);
        game.getBoards().get(1).getShips().add(ship2);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        ShotResult result = gameService.shoot(gameId, player1, x, y);

        assertEquals(ShotResult.HIT, result);
    }

    @Test
    public void shootSuccessDestroyed() {
        Long gameId = 1L;
        ship1.getDecks().get(0).setId(1L);
        game.getBoards().get(1).getShips().add(ship1);
        game.getBoards().get(1).getShips().add(ship2);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        ShotResult result = gameService.shoot(gameId, player1, x, y);

        assertEquals(ShotResult.DESTROYED, result);
    }

    @Test
    public void shootSuccessWin() {
        Long gameId = 1L;
        ship1.getDecks().get(0).setId(1L);
        game.getBoards().get(1).getShips().add(ship1);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        ShotResult result = gameService.shoot(gameId, player1, x, y);

        assertEquals(ShotResult.WIN, result);
    }

    @Test
    public void shootSuccessMiss() {
        Long gameId = 1L;
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        ShotResult result = gameService.shoot(gameId, player1, x, y);

        assertEquals(ShotResult.MISS, result);
        assertTrue(game.isTurnOfSecondPlayer());
    }

    @Test
    public void shootCoordinatesException() {
        Long gameId = 1L;
        int x = 11;
        int y = -1;
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        CoordinatesException exc = assertThrows(CoordinatesException.class, () -> gameService.shoot(gameId, player1, x, y));
        assertEquals("Wrong coordinates", exc.getMessage());
    }

    @Test
    public void shootGameNotFoundException() {
        Long gameId = 132L;
        game.setWinnerId(player1);
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        GameNotFoundException exc = assertThrows(GameNotFoundException.class, () -> gameService.shoot(gameId, player1, x, y));
        assertEquals("Game with ID 132 not found", exc.getMessage());
    }

    @Test
    public void shootGameNotFoundExceptionForGameOver() {
        Long gameId = 123L;
        game.setId(gameId);
        game.setWinnerId(player1);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        GameNotFoundException exc = assertThrows(GameNotFoundException.class, () -> gameService.shoot(gameId, player1, x, y));
        assertEquals("Game with ID 123 is over", exc.getMessage());
    }

    @Test
    public void shootWrongTurnException() {
        Long gameId = 1L;

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        WrongTurnException exc = assertThrows(WrongTurnException.class,
                () -> gameService.shoot(gameId, player2, x, y));
        assertEquals("Now is turn of the other player", exc.getMessage());
    }

    @Test
    public void shootPlayerNotFoundException() {
        Long gameId = 55L;
        player2 = 3L;
        game.setId(gameId);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        PlayerNotFoundException exc = assertThrows(PlayerNotFoundException.class,
                () -> gameService.shoot(gameId, player2, x, y));
        assertEquals("Player with ID 3 not found in game with ID 55", exc.getMessage());
    }

}