package ru.prbank.test_task.seacombat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prbank.test_task.seacombat.domain.enums.ShotResult;
import ru.prbank.test_task.seacombat.domain.exception.*;
import ru.prbank.test_task.seacombat.domain.model.Deck;
import ru.prbank.test_task.seacombat.domain.model.Game;
import ru.prbank.test_task.seacombat.domain.model.PlayingBoard;
import ru.prbank.test_task.seacombat.domain.model.Ship;
import ru.prbank.test_task.seacombat.repository.GameRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Реализация сервиса для работы с игрой.
 */
@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final ShipService shipService;

    /**
     * Реализация метода {@link GameService#getGame(Long)}
     * См. описание метода в интерфейсе {@link GameService}
     */
    @Override
    public Game getGame(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with ID " + gameId + " not found"));
    }
    /**
     * Реализация метода {@link GameService#createGame(Long, Long)} с предварительной валидацией.
     * См. описание метода в интерфейсе {@link GameService}
     *
     * @throws PlayerNotFoundException исключение пробрасывается дальше по стеку и обрабатывается контроллером.
     */
    @Override
    public Long createGame(Long player1, Long player2) throws PlayerNotFoundException {
        validatePlayersId(player1, player2);
        // Создание игры
        Game game = new Game(player1, player2);
        // Сохранение игры в базу данных
        Game savedGame = gameRepository.save(game);
        return savedGame.getId();
    }

    /**
     * Реализация метода {@link GameService#putShip(Long, Long, int, int, int, int)} с предварительной валидацией.
     * См. описание метода в интерфейсе {@link GameService}
     *
     * @throws RuntimeException полный список исключений см. в описании метода в интерфейсе.
     * Исключения пробрасываются дальше по стеку и обрабатываются контроллером.
     */
    @Override
    public void putShip(Long gameId, Long playerId, int headX, int headY, int tailX, int tailY) throws RuntimeException{
        // Проверка существования игры по ID
        Game game = getGame(gameId);

        validateGameAndPlayer(game, playerId);

        Ship ship = new Ship(fillDecks(headX, headY, tailX, tailY));

        PlayingBoard boardForPut = game.getBoards().stream()
                .filter(b-> Objects.equals(b.getOwnerId(), playerId))
                .findFirst().orElseThrow(() -> new BoardNotFoundException("Board not found for player with ID " + playerId));
        shipService.validateShip(boardForPut, ship);

        // Размещение корабля на доске
        boardForPut.getShips().add(ship);

        // Обновление состояния игры в БД
        gameRepository.save(game);
    }

    private List<Deck> fillDecks(int headX, int headY, int tailX, int tailY) throws CoordinatesException{
        boolean isVertical = false;
        if (headX == tailX) isVertical = true;
        else if (headY != tailY) throw new CoordinatesException("Ship coordinates must be at one line");

        List<Deck> decks = new ArrayList<>();

        if (isVertical){
            for (int y = headY, index = 0; y <= tailY; y++, index++) {
                decks.add(index, new Deck(headX, y));
            }
        }else {
            for (int x = headX, index = 0; x <= tailX; x++, index++) {
                decks.add(index, new Deck(x, headY));
            }
        }

        return decks;
    }

    /**
     * Реализация метода {@link GameService#shoot(Long, Long, int, int)} с предварительной валидацией.
     * См. описание метода в интерфейсе {@link GameService}
     *
     * @throws RuntimeException полный список исключений см. в описании метода в интерфейсе.
     * Исключения пробрасываются дальше по стеку и обрабатываются контроллером.
     */
    @Override
    public ShotResult shoot(Long gameId, Long playerId, int x, int y) throws RuntimeException{
        // Проверка существования игры по ID
        Game game = getGame(gameId);
        // Другие проверки
        validateMove(game, playerId, x, y);

        ShotResult response = ShotResult.MISS;
        PlayingBoard boardForHit = game.isTurnOfSecondPlayer() ? game.getBoards().get(0) : game.getBoards().get(1);

        // проверка попадания
        Long hitDeckId = hitDeckId(boardForHit, x, y);
        if (hitDeckId != -1L) {
            response = ShotResult.HIT;
            //получить корабль по id deck
            Ship hitShip = boardForHit.getShips().stream()
                    .filter(s -> s.getDecks().stream()
                            .anyMatch(d -> Objects.equals(d.getId(), hitDeckId)))
                    .findFirst().orElseThrow(()-> new ShipNotFoundException("Ship with Deck ID " + hitDeckId + " not found"));
            // если не осталось живых дек - убит
            if (hitShip.getDecks().stream().allMatch(Deck::isHit)) {
                response = ShotResult.DESTROYED;
                hitShip.setDestroyed(true);
                // если все корабли подбиты - победа
                if (boardForHit.getShips().stream().allMatch(Ship::isDestroyed)) {
                    response = ShotResult.WIN;
                    game.setWinnerId(playerId);
                }
            }
        }
        // смена хода
        if (response == ShotResult.MISS) {
            game.changeTurn();
        }
        // Обновление состояния игры в БД
        gameRepository.save(game);

        return response;
    }

    private Long hitDeckId(PlayingBoard boardForHit, int x, int y) {
        List<Deck> decks = boardForHit.getShips().stream()
                .flatMap(it -> it.getDecks().stream())
                .toList();
        for (Deck deck : decks) {
            if (deck.getCoordinateX() == x && deck.getCoordinateY() == y && !deck.isHit()) {
                deck.setHit(true);
                return deck.getId();
            }
        }
        return -1L;
    }

    private void validatePlayersId(Long player1, Long player2) throws PlayerNotFoundException {
        if (Objects.equals(player1, player2)) throw new PlayerNotFoundException("Player can not play with himself");
        Long[] players = {player1, player2};
        for (Long id : players) {
            if (id == null || id < 1 || !playerService.isPlayerExists(id)) {
                throw new PlayerNotFoundException("Player with ID " + id + " not found");
            }
        }
    }

    private void validateMove(Game game, Long playerId, int x, int y) throws RuntimeException {
        validateGameAndPlayer(game, playerId);
        // Проверка чей ход
        Long whichTurnIsNow = game.isTurnOfSecondPlayer() ? game.getBoards().get(1).getOwnerId() :
                game.getBoards().get(0).getOwnerId();
        if (!whichTurnIsNow.equals(playerId)) throw new WrongTurnException("Now is turn of the other player");
        // Проверка корректности координат
        if (x < 1 || x > PlayingBoard.FIELD_SIZE || y < 1 || y > PlayingBoard.FIELD_SIZE) {
            throw new CoordinatesException("Wrong coordinates");
        }
    }

    private void validateGameAndPlayer(Game game, Long playerId) throws RuntimeException {
        // Проверка что игра не закончена
        if (game.getWinnerId() != -1) throw new GameNotFoundException("Game with ID " + game.getId() + " is over");
        // Проверка что игрок есть в игре
        if (game.getBoards().stream().noneMatch(board -> Objects.equals(board.getOwnerId(), playerId))) {
            throw new PlayerNotFoundException("Player with ID " + playerId + " not found in game with ID " + game.getId());
        }
    }
}
