package ru.prbank.test_task.seacombat.service;

public interface PlayerService {
    // проверка что игрок есть в БД
    boolean isPlayerExists(Long playerId);
}
