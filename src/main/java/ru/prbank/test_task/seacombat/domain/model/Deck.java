package ru.prbank.test_task.seacombat.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность в БД. Описывает деку корабля по координатам X и Y, индикатор повреждения - поле isHit.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "decks")
@Schema(name = "Deck", description = "One deck is one part of the ship.")
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "coordinate_x")
    private int coordinateX;
    @Column(name = "coordinate_y")
    private int coordinateY;
    @Column(name = "is_hit")
    private boolean isHit;

    public Deck(int coordinateX, int coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    @Override
    public String toString() {
        return "(" + coordinateX + ", " + coordinateY + ")";
    }
}
