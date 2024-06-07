package ru.prbank.test_task.seacombat.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class GameTest {
    @Test
    public void changeTurn() {
        Game game = new Game(1L, 2L);
        game.setTurnOfSecondPlayer(true);
        game.changeTurn();

        assertFalse(game.isTurnOfSecondPlayer());
    }
}