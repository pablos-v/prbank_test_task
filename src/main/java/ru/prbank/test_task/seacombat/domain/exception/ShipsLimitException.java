package ru.prbank.test_task.seacombat.domain.exception;

public class ShipsLimitException extends RuntimeException {
    public ShipsLimitException(String message) {
        super(message);
    }
}
