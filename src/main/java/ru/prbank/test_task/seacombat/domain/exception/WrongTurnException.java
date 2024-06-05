package ru.prbank.test_task.seacombat.domain.exception;

public class WrongTurnException extends RuntimeException {
    public WrongTurnException(String message) {
        super(message);
    }
}
