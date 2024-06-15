package ru.prbank.test_task.seacombat.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.prbank.test_task.seacombat.domain.enums.ShotResult;
import ru.prbank.test_task.seacombat.domain.exception.CoordinatesException;
import ru.prbank.test_task.seacombat.domain.exception.GameNotFoundException;
import ru.prbank.test_task.seacombat.domain.exception.PlayerNotFoundException;
import ru.prbank.test_task.seacombat.domain.model.Ship;
import ru.prbank.test_task.seacombat.service.GameService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MainControllerTest {

    GameService gameService;
    MainController mainController;
    Long player1;
    Long player2;
    Long gameId;
    Ship ship;
    @BeforeEach
    void init(){
        gameService = mock(GameService.class);
        mainController = new MainController(gameService);
        player1 = 1L;
        player2 = 2L;
        gameId = 100L;
        ship = new Ship();
    }

    @Test
    public void startGameSuccess() {
        when(gameService.createGame(player1, player2)).thenReturn(gameId);

        ResponseEntity<?> response = mainController.startGame(player1, player2);

        assertEquals(ResponseEntity.ok().body(gameId), response);

    }

    @Test
    public void startGamePlayerNotFoundException()  {
        when(gameService.createGame(player1, player2)).thenThrow(new PlayerNotFoundException("First player not found"));

        ResponseEntity<?> response = mainController.startGame(player1, player2);

        assertEquals(ResponseEntity.badRequest().body("First player not found"), response);
    }

    @Test
    public void putShipSuccess() {
        doNothing().when(gameService).putShip(gameId, player1, 1, 1, 1 ,1);

        ResponseEntity<?> response = mainController.putShip(gameId, player1, 1, 1, 1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void putShipGameNotFoundException()   {
        doThrow(new GameNotFoundException("Game not found")).when(gameService).putShip(gameId, player1,  1, 1, 1, 1);

        ResponseEntity<?> response = mainController.putShip(gameId, player1,  1, 1, 1, 1);

        assertEquals(ResponseEntity.badRequest().body("Game not found"), response);
    }

    @Test
    public void shootSuccess() {
        int x = 5;
        int y = 5;
        when(gameService.shoot(gameId, player1, x, y)).thenReturn(ShotResult.HIT);

        ResponseEntity<?> response = mainController.shoot(gameId, player1, x, y);

        assertEquals(ResponseEntity.ok().body("HIT"), response);
    }

    @Test
    public void test_shooting_at_coordinates_outside_the_board_boundaries() {
        int x = 11;
        int y = 11;
        when(gameService.shoot(gameId, player1, x, y)).thenThrow(new CoordinatesException("Wrong coordinates"));

        ResponseEntity<?> response = mainController.shoot(gameId, player1, x, y);

        assertEquals(ResponseEntity.badRequest().body("Wrong coordinates"), response);
    }
}