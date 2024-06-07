package ru.prbank.test_task.seacombat.domain.exception;

public class ShipNotFoundException extends RuntimeException {
    public ShipNotFoundException(String message) {
        super(message);
    }
}
