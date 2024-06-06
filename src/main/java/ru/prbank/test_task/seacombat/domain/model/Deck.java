package ru.prbank.test_task.seacombat.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "decks")
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "coordinate_x")
    private int coordinateX;
    @Column(name = "coordinate_y")
    private int coordinateY;
    @Column(name = "is_hit")
    private boolean isHit = false;

    @Override
    public String toString() {
        return "(" + coordinateX + ", " + coordinateY + ")";
    }
}
