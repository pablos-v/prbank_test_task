package ru.prbank.test_task.seacombat.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.prbank.test_task.seacombat.domain.enums.ShotResult;
import ru.prbank.test_task.seacombat.domain.exception.PlayerNotFoundException;
import ru.prbank.test_task.seacombat.domain.model.Ship;
import ru.prbank.test_task.seacombat.service.GameService;


@RestController
@RequestMapping("/sea_combat")
@AllArgsConstructor
@Tag(name = "sea_combat")
public class MainController {
    private final GameService gameService;

    /**
     * Создание новой игры для двух игроков. Предполагается, что игроки уже созданы и есть в БД.
     * @param firstPlayerId Id первого игрока
     * @param secondPlayerId Id второго игрока
     * @return Id игры
     */
    @PostMapping("/start/{firstPlayerId}/{secondPlayerId}")
    @Operation(summary = "Creates new game for two players by ID.", description= "Expected that users exists.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "User not found")})
    public ResponseEntity<?> startGame(@PathVariable @NotNull @Positive Long firstPlayerId, @PathVariable @NotNull @Positive Long secondPlayerId) {
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
     * @param gameId Id игры
     * @param playerId Id игрока, который выставляет корабль
     * @param ship JSON - описание корабля
     * @return HTTP status code
     */
    @PostMapping("/{gameId}/{playerId}/put_ship")
    @Operation(summary = "Puts a ship into the playing board of selected user in selected game."
            , description= "Ship description is expected in JSON body; playing board size 10*10; ship should not cross " +
            "position of the other ship or have a common cells-neighbours. Possible ships, size/amount: 4/1, 3/2, 2/3, 1/4.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Game with ID {game_id} not found"),
            @ApiResponse(responseCode = "400", description = "Game with ID {game_id} is over"),
            @ApiResponse(responseCode = "400", description = "Player with ID {player_id} not found in game with ID {game_id}"),
            @ApiResponse(responseCode = "400", description = "No more ships of size {ship_size} is allowed"),
            @ApiResponse(responseCode = "400", description = "Ship with decks: {Decks[]} is not straight"),
            @ApiResponse(responseCode = "400", description = "Ship with decks: {Decks[]} is not solid"),
            @ApiResponse(responseCode = "400", description = "Ship with decks: {Decks[]} is out of field bounds"),
            @ApiResponse(responseCode = "400", description = "Ship with decks: {Decks[]} could not be too close to existing ships")})
    public ResponseEntity<?> putShip(@PathVariable Long gameId, @PathVariable Long playerId, @RequestBody Ship ship) {
        try {
            gameService.putShip(gameId, playerId, ship);
        } catch (RuntimeException e) {
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
    @Operation(summary = "Make shoot at the selected coordinates of selected user`s board in selected game."
            , description= "When hit some deck, then player shoot again, otherwise - turn of the other player.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MISS"),
            @ApiResponse(responseCode = "200", description = "HIT"),
            @ApiResponse(responseCode = "200", description = "DESTROYED"),
            @ApiResponse(responseCode = "200", description = "WIN"),
            @ApiResponse(responseCode = "400", description = "Game with ID {game_id} not found"),
            @ApiResponse(responseCode = "400", description = "Game with ID {game_id} is over"),
            @ApiResponse(responseCode = "400", description = "Player with ID {player_id} not found in game with ID {game_id}"),
            @ApiResponse(responseCode = "400", description = "Now is turn of the other player"),
            @ApiResponse(responseCode = "400", description = "Wrong coordinates")})
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