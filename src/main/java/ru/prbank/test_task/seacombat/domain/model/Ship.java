package ru.prbank.test_task.seacombat.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * Класс описывает корабль
 */
@Data
@Entity
@Table(name = "ships")
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "is_destroyed")
    private boolean isDestroyed = false;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ship_id")
    private List<Deck> decks;

    public boolean isStraight() {
        List<Deck> decks = getDecks();
        if (decks.size() == 1) return true;
        int firstPointX = decks.get(0).getCoordinateX();
        int firstPointY = decks.get(0).getCoordinateY();
        // все точки одного из измерений должны быть на одной прямой
        return decks.stream().allMatch(deck -> deck.getCoordinateX() == firstPointX)
                || decks.stream().allMatch(deck -> deck.getCoordinateY() == firstPointY);
    }

    public boolean isSolid() {
        List<Deck> decks = getDecks();
        if (decks.size() == 1) return true;
        int[] dimensionX = decks.stream().mapToInt(Deck::getCoordinateX).toArray();
        int[] dimensionY = decks.stream().mapToInt(Deck::getCoordinateY).toArray();
        // все точки одного из измерений должны идти последовательно (1,2) (1,3) (1,4)
        return isConsistent(dimensionX) || isConsistent(dimensionY);
    }

    private boolean isConsistent(int[] dimension) {
        for (int i = 0; i < dimension.length - 1; i++) {
            if (dimension[i] + 1 != dimension[i + 1]) return false;
        }
        return true;
    }

    public boolean isInBounds() {
        int maxX = decks.stream().mapToInt(Deck::getCoordinateX).max().orElse(0);
        int maxY = decks.stream().mapToInt(Deck::getCoordinateY).max().orElse(0);
        int minX = decks.stream().mapToInt(Deck::getCoordinateX).min().orElse(0);
        int minY = decks.stream().mapToInt(Deck::getCoordinateY).min().orElse(0);
        return minX > 0 && minY > 0 && maxX <= PlayingBoard.FIELD_SIZE && maxY <= PlayingBoard.FIELD_SIZE;
    }
}
