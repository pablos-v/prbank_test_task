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
    private int coordinateX;
    private int coordinateY;
    private boolean isHit;
}
