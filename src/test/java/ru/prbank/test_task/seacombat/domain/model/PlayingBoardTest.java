package ru.prbank.test_task.seacombat.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayingBoardTest {
    PlayingBoard board;

    @BeforeEach
    void init(){
        board = new PlayingBoard(1L);
    }

    @Test
    public void isShipLimitReached() {
        board.setShips(List.of(
                new Ship(List.of(new Deck(1, 1))),
                new Ship(List.of(new Deck(2, 2))),
                new Ship(List.of(new Deck(3, 3))),
                new Ship(List.of(new Deck(4, 4)))
        ));

        assertTrue(board.isShipLimitReached(1));
    }

    @Test
    public void isNotShipLimitReached() {
        board.setShips(List.of(
                new Ship(List.of(new Deck(1, 1))))
        );

        assertFalse(board.isShipLimitReached(1));
    }

    @Test
    public void isShipLimitReachedForNull() {
        board.setShips(null);

        assertTrue(board.isShipLimitReached(1));
    }

    @Test
    public void isShipLimitReachedForZeroDecks()  {

        assertTrue(board.isShipLimitReached(0));
    }
}