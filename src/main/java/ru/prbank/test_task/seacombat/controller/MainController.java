package ru.prbank.test_task.seacombat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.prbank.test_task.seacombat.domain.enums.ShotResult;
import ru.prbank.test_task.seacombat.domain.exception.GameNotFoundException;
import ru.prbank.test_task.seacombat.service.GameService;
import ru.prbank.test_task.seacombat.domain.model.Ship;
import ru.prbank.test_task.seacombat.domain.model.Game;


@RestController
@RequestMapping("/sea_combat")
public class MainController {
    GameService gameService;

    @PostMapping("/start") // /start/?player1=ivan&player2=olga
    public ResponseEntity<Long> startGame(@RequestParam("player1") String player1Name,
            @RequestParam("player2") String player2Name) {
        // TODO проверка входных данных на валидность
        // varchar 50?

        // создание игры
        Long gameId = gameService.createGame(player1Name, player2Name);

        return ResponseEntity.ok(gameId);
    }

    @PostMapping("/{gameId}/{playerId}/put_ship")
    public ResponseEntity<?> putShip(@PathVariable Long gameId, @PathVariable Long playerId, @RequestBody Ship ship) {
        // Проверка существования игры по ID
        Game game = gameService.getGame(gameId);  // TODO обработать Искл (ResponseEntity.internalServerError())

        // TODO проверить что игра не закончена

        // TODO проверить есть ли игрок в игре

        // TODO Проверка, что корабль не гнутый

        // TODO Проверка, что корабль не липнет к соседям

        // TODO достать доску игрока

        // TODO Проверка, что игрок не пытается разместить больше кораблей, чем разрешено
        if (game.getShips().size() >= MAX_SHIPS_PER_PLAYER) {
            return ResponseEntity.badRequest().body("Maximum number of ships reached");
        }

        // TODO Проверка, что корабль не выходит за пределы поля
        if (!isValidPosition(ship)) {
            return ResponseEntity.badRequest().body("Invalid position for ship");
        }

        // Размещение корабля на доске
        game.getBoards().get();

        // Обновление состояния игры в базе данных
        gameRepository.save(game);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{gameId}/{playerId}/shoot/{x}/{y}")
    public ResponseEntity<?> shoot(
            @PathVariable Long gameId, 
            @PathVariable Long playerId, 
            @PathVariable int x,
            @PathVariable int y
    ) {

        ShotResult response;
        try {
            response = gameService.shoot(gameId, playerId, x, y);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(response.toString());
    }
}