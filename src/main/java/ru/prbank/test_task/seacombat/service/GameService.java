package ru.prbank.test_task.seacombat.service;

import ru.prbank.test_task.seacombat.domain.enums.ShotResult;
import ru.prbank.test_task.seacombat.domain.exception.*;
import ru.prbank.test_task.seacombat.domain.model.Game;
import ru.prbank.test_task.seacombat.domain.model.Ship;

/**
 * Интерфейс взаимодействия с игрой.
 */
public interface GameService {
    /**
     * Создание игры для двух игроков. Метод обновляет состояние БД.
     *
     * @param player1 ID 1 игрока
     * @param player2 ID 2 игрока
     * @return ID созданной игры
     * @throws PlayerNotFoundException если хотя бы один из игроков не существует
     */
    Long createGame(Long player1, Long player2) throws PlayerNotFoundException;

    /**
     * Получение игры.
     *
     * @param gameId ID игры
     * @return Объект игры, в который каскадно Lazy подтягивается информация о полях игроков, кораблях в них, и деках
     * кораблей.
     */
    Game getGame(Long gameId);

    /**
     * Расположить корабль на игровой доске. Доска для расположения определяется по ID игрока в игре.
     * Метод обновляет состояние БД.
     *
     * @param gameId   ID игры.
     * @param playerId ID игрока.
     * @param ship     Объект располагаемого корабля.
     * @throws PlayerNotFoundException если игрок не существует.
     * @throws GameNotFoundException   если игра не существует.
     * @throws CoordinatesException    если координаты корабля не верные.
     * @throws ShipFormException       если форма корабля не прямая, или он не цельный.
     * @throws ShipsLimitException     если количество кораблей больше максимального.
     * @throws ShipNotFoundException   если корабль не найден.
     * @throws BoardNotFoundException  если игровой доски не существует.
     */
    void putShip(Long gameId, Long playerId, Ship ship) throws PlayerNotFoundException, GameNotFoundException,
            CoordinatesException, ShipFormException, ShipsLimitException, BoardNotFoundException, ShipNotFoundException;

    /**
     * Сделать выстрел. Стреляющий игрок должен быть участником в данной игре. Метод обновляет состояние БД.
     *
     * @param gameId   ID игры.
     * @param playerId ID игрока.
     * @param x        Координата выстрела по оси X.
     * @param y        Координата выстрела по оси Y.
     * @return Результат выстрела - одно из значений: MISS (мимо), HIT (попал), DESTROYED (убил), WIN (победа).
     * @throws PlayerNotFoundException если игрок не существует.
     * @throws GameNotFoundException   если игра не существует.
     * @throws CoordinatesException    если координаты корабля не верные.
     * @throws WrongTurnException      если право сделать ход у другого игрока.
     */
    ShotResult shoot(Long gameId, Long playerId, int x, int y) throws PlayerNotFoundException, GameNotFoundException,
            CoordinatesException, WrongTurnException;

}
