package ru.prbank.test_task.seacombat.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShipTest {
    Ship ship;

    @BeforeEach
    void init() {
        ship = new Ship();
    }

    @Test
    public void testSingleIsStraight() {
        ship.setDecks(List.of(new Deck(1, 1)));

        assertTrue(ship.isStraight());
    }

    @Test
    public void testIsStraight() {
        ship.setDecks(List.of(new Deck(1, 1), new Deck(1, 2), new Deck(1, 3)));

        assertTrue(ship.isStraight());
    }

    @Test
    public void testIsNotStraight() {
        ship.setDecks(List.of(new Deck(1, 1), new Deck(1, 2), new Deck(2, 2)));

        assertFalse(ship.isStraight());
    }
    @Test
    public void testIsNotStraightForNull() {
        ship.setDecks(null);

        assertFalse(ship.isStraight());
    }
    @Test
    public void testIsNotStraightForEmpty() {
        ship.setDecks(List.of());

        assertFalse(ship.isStraight());
    }

    @Test
    public void testSingleIsSolid() {
        ship.setDecks(List.of(new Deck(1, 1)));

        assertTrue(ship.isSolid());
    }

    @Test
    public void testIsSolid() {
        ship.setDecks(List.of(new Deck(1, 1), new Deck(1, 2)));

        assertTrue(ship.isSolid());
    }

    @Test
    public void testIsNotSolid() {
        ship.setDecks(List.of(new Deck(1, 1), new Deck(1, 3)));

        assertFalse(ship.isSolid());
    }

    @Test
    public void testIsNotSolidForNull() {
        ship.setDecks(null);

        assertFalse(ship.isSolid());
    }

    @Test
    public void testIsNotSolidForEmpty() {
        ship.setDecks(List.of());

        assertFalse(ship.isSolid());
    }

    @Test
    public void isInBounds() {
        ship.setDecks(List.of(new Deck(1, 1), new Deck(1, 2), new Deck(1, 3)));

        assertTrue(ship.isInBounds());
    }
    @Test
    public void isNotInBounds() {
        ship.setDecks(List.of(new Deck(-1, 1), new Deck(1, 11), new Deck(1, 3)));

        assertFalse(ship.isInBounds());
    }
    @Test
    public void isNotInBoundsForNull() {
        ship.setDecks(null);

        assertFalse(ship.isInBounds());
    }
    @Test
    public void isNotInBoundsForEmpty() {
        ship.setDecks(List.of());

        assertFalse(ship.isInBounds());
    }



}