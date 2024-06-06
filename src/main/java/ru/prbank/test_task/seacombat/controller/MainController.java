package ru.prbank.test_task.seacombat.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.prbank.test_task.seacombat.domain.enums.ShotResult;
import ru.prbank.test_task.seacombat.domain.exception.PlayerNotFoundException;
import ru.prbank.test_task.seacombat.service.GameService;
import ru.prbank.test_task.seacombat.domain.model.Ship;
import ru.prbank.test_task.seacombat.domain.model.Game;


@RestController
@RequestMapping("/sea_combat")
@AllArgsConstructor
public class MainController {
    private final GameService gameService;

    /**
     * Создание новой игры для двух игроков. Предполагается, что игроки уже созданы и есть в БД.
     * @param firstPlayerId Id первого игрока
     * @param secondPlayerId Id второго игрока
     * @return Id игры
     */
    @PostMapping("/start/{firstPlayerId}/{secondPlayerId}")
    public ResponseEntity<?> startGame(@PathVariable Long firstPlayerId, @PathVariable Long secondPlayerId) {
        Long gameId;
        try {
            gameId = gameService.createGame(firstPlayerId, secondPlayerId);
        } catch (PlayerNotFoundException e)  {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(gameId);
    }

    /**
     * Установка корабля на игровом поле соответствующего игрока.
     * Размер поля по умолчанию 10, предполагается система координат от 1 до 10 включительно.
     * Корабль передаётся в теле запроса и выглядит примерно так:
     * {
     *   "id": 1,
     *   "isDestroyed": false,
     *   "decks": [
     *     {
     *       "id": 1,
     *       "coordinateX": 1,
     *       "coordinateY": 1,
     *       "isHit": false
     *     },
     *     {
     *       "id": 2,
     *       "coordinateX": 1,
     *       "coordinateY": 2,
     *       "isHit": false
     *     }
     *   ]
     * }
     * @param gameId Id игры
     * @param playerId Id игрока, который выставляет корабль
     * @param ship JSON - описание корабля
     * @return HTTP status code
     */
    @PostMapping("/{gameId}/{playerId}/put_ship")
    public ResponseEntity<?> putShip(@PathVariable Long gameId, @PathVariable Long playerId, @RequestBody Ship ship) {
        try {
            gameService.putShip(gameId, playerId, ship);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Произведение выстрела указанным игроком по указанным координатам игрового поля соперника.
     * @param gameId Id игры
     * @param playerId Id стреляющего игрока
     * @param x координата x
     * @param y координата y
     * @return возвращает один из вариантов исхода: ранен, убит, мимо, победа
     */
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