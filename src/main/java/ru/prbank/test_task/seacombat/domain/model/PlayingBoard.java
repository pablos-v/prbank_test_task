package ru.prbank.test_task.seacombat.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Сущность в БД. Описывает игровую доску, имеет список кораблей на ней, и содержит параметры размера доски и количества типов кораблей.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "boards")
public class PlayingBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "owner")
    private Long ownerId;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id", nullable  = false)
    private List<Ship> ships;

    public static final int FIELD_SIZE = 10;
    private static final int MAX_ALLOWED_1_DECK_SHIPS = 4;
    private static final int MAX_ALLOWED_2_DECK_SHIPS = 3;
    private static final int MAX_ALLOWED_3_DECK_SHIPS = 2;
    private static final int MAX_ALLOWED_4_DECK_SHIPS = 1;

    public PlayingBoard(Long ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Проверка кол-ва кораблей по их типам.
     * @param deckCount - тип корабля (кол-во его дек).
     * @return - true, если кол-во кораблей превышает максимальное кол-во, иначе false.
     */
    public boolean isShipLimitReached(int deckCount) {
        if (ships == null || deckCount < 1 || deckCount > 4) return true;
        long countShipsOfThatSize = ships.stream().filter(it -> it.getDecks().size() == deckCount).count();
        return countShipsOfThatSize >= maxAmountOfShips(deckCount);
    }

    private int maxAmountOfShips(int size) {
        return switch (size) {
            case 1 -> MAX_ALLOWED_1_DECK_SHIPS;
            case 2 -> MAX_ALLOWED_2_DECK_SHIPS;
            case 3 -> MAX_ALLOWED_3_DECK_SHIPS;
            case 4 -> MAX_ALLOWED_4_DECK_SHIPS;
            default -> 0;
        };
    }
}
