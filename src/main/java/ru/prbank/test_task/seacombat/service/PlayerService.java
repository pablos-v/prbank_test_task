package ru.prbank.test_task.seacombat.service;

/**
 * Интерфейс взаимодействия с игроками.
 */
public interface PlayerService {
    /**
     * Проверка существования игрока.
     * @param playerId ID игрока.
     * @return true, если игрок существует в БД.
     */
    boolean isPlayerExists(Long playerId);
}
