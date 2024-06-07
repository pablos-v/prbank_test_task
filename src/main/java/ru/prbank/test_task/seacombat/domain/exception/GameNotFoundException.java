package ru.prbank.test_task.seacombat.domain.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String message) {
        super(message);

    }
}
