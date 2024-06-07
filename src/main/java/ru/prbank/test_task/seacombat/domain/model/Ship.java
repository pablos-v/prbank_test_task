package ru.prbank.test_task.seacombat.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Сущность в БД. Описывает корабль, имеет методы самопроверки.
 */
@Data
@Entity
@Table(name = "ships")
@Schema(name = "Ship", description = "One of the main entities in the game. No more ships left - you loose.")
@NoArgsConstructor
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "is_destroyed")
    private boolean isDestroyed;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ship_id")
    private List<Deck> decks;

    public Ship(List<Deck> decks) {
        this.decks = decks;
    }

    /**
     * Проверка что корабль прямой, не изгибается.
     * @return true если деки корабля расположены на одной прямой по вертикали или горизонтали.
     */
    public boolean isStraight() {
        if (decks == null || decks.isEmpty()) return false;
        if (decks.size() == 1) return true;
        int firstPointX = decks.get(0).getCoordinateX();
        int firstPointY = decks.get(0).getCoordinateY();
        // все точки одного из измерений должны быть на одной прямой
        return decks.stream().allMatch(deck -> deck.getCoordinateX() == firstPointX)
                || decks.stream().allMatch(deck -> deck.getCoordinateY() == firstPointY);
    }

    /**
     * Проверка что корабль цельный.
     * @return true если деки корабля расположены последовательно, без прерываний.
     */
    public boolean isSolid() {
        if (decks == null || decks.isEmpty()) return false;
        if (decks.size() == 1) return true;
        int[] dimensionX = decks.stream().mapToInt(Deck::getCoordinateX).toArray();
        int[] dimensionY = decks.stream().mapToInt(Deck::getCoordinateY).toArray();
        // Все точки одного из измерений должны идти последовательно (1,2) (1,3) (1,4).
        return isConsistent(dimensionX) || isConsistent(dimensionY);
    }

    /**
     * Проверка массива на последовательность.
     * @param dimension - массив значений одной из координат.
     * @return true если значения идут последовательно.
     */
    private boolean isConsistent(int[] dimension) {
        for (int i = 0; i < dimension.length - 1; i++) {
            if (dimension[i] + 1 != dimension[i + 1]) return false;
        }
        return true;
    }

    /**
     * Проверка что корабль в границах игрового поля.
     * @return true если предельные координаты не выходят за ограничения поля.
     */
    public boolean isInBounds() {
        if (decks == null || decks.isEmpty()) return false;
        int maxX = decks.stream().mapToInt(Deck::getCoordinateX).max().orElse(0);
        int maxY = decks.stream().mapToInt(Deck::getCoordinateY).max().orElse(0);
        int minX = decks.stream().mapToInt(Deck::getCoordinateX).min().orElse(0);
        int minY = decks.stream().mapToInt(Deck::getCoordinateY).min().orElse(0);
        return minX > 0 && minY > 0 && maxX <= PlayingBoard.FIELD_SIZE && maxY <= PlayingBoard.FIELD_SIZE;
    }
}
