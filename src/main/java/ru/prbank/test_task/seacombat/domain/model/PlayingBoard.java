package ru.prbank.test_task.seacombat.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
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

    public boolean amountExceeded(int checkingSize) {
        long countShipsOfThatSize = ships.stream().filter(it -> it.getDecks().size() == checkingSize).count();
        return countShipsOfThatSize >= maxAmountOfShips(checkingSize);
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
