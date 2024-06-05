package ru.prbank.test_task.seacombat.service;

import org.springframework.stereotype.Service;
import ru.prbank.test_task.seacombat.domain.enums.ShotResult;
import ru.prbank.test_task.seacombat.domain.exception.GameNotFoundException;
import ru.prbank.test_task.seacombat.domain.exception.PlayerNotFoundException;
import ru.prbank.test_task.seacombat.domain.exception.WrongTurnException;
import ru.prbank.test_task.seacombat.domain.model.Game;
import ru.prbank.test_task.seacombat.domain.model.PlayingBoard;
import ru.prbank.test_task.seacombat.domain.model.Ship;
import ru.prbank.test_task.seacombat.repository.GameRepository;

import java.util.List;
import java.util.Objects;

@Service
public class GameServiceImpl implements GameService {
    GameRepository gameRepository;

    @Override
    public Long createGame(String player1Name, String player2Name) {
        // TODO есть ли игроки в БД, если нет, то создать

        // Создание досок


        // Создание игры
        Game game = new Game(player1.getId(), player2.getId());

        // Сохранение игры в базу данных
        Game savedGame = gameRepository.save(game);

        return savedGame.getId();
    }

    @Override
    public Game getGame(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game with id " + gameId + " not found"));
    }

    @Override
    public ShotResult shoot(Long gameId, Long playerId, int x, int y) {
        // Проверка существования игры по ID
        Game game = getGame(gameId);
        // Другие проверки
        validateMove(game, playerId, x, y);

        // TODO ранен, убит, мимо, победа.
        ShotResult response = ShotResult.MISS;
        PlayingBoard board  = game.isTurnOfSecondPlayer()? game.getBoards().get(1): game.getBoards().get(0);
        if (game.getBoards()) {  // Ранен
            if () { // убит
                if () { //победа
                    response = ShotResult.WIN;
                    game.setOver(true);
                } response = ShotResult.DESTROYED;
                // set to ship status Destroyed
            } response = ShotResult.HIT;
            // set to deck status HIT
        } if (response == ShotResult.MISS) {
            game.changeTurn();
        }
        // Обновление состояния игры в БД
        gameRepository.save(game);

        return response;
    }

    private void validateMove(Game game, Long playerId, int x, int y) {
        // Проверка что игра не закончена
        if (game.isOver()) throw new GameNotFoundException("Game with ID " + game.getId() + " is over");
        // Проверка что игрок есть в игре
        if (game.getBoards().stream().noneMatch(board -> Objects.equals(board.getOwnerId(), playerId))) {
            throw new PlayerNotFoundException("Player with ID  " + playerId + " not found in game with ID " + game.getId());
        }
        // Проверка чей ход
        Long whosTurnIsNow = game.isTurnOfSecondPlayer() ? game.getBoards().get(1).getId() :
                game.getBoards().get(0).getId();
        if (!whosTurnIsNow.equals(playerId)) throw new WrongTurnException("Now is turn of the other player");
        // Проверка корректности координат
        if (x < 1 || x > 10 || y < 1 || y > 10) {
            throw new IllegalArgumentException("Wrong coordinates");
        }
    }
}
