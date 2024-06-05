package ru.prbank.test_task.seacombat.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.prbank.test_task.seacombat.domain.enums.PositionOnField;

import java.util.List;

@Data
//@NoArgsConstructor
@Entity
@Table(name = "ships")
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "is_destroyed")
    private boolean isDestroyed;

    @OneToMany
    private List<Deck> decks;

    @ManyToOne
    private PlayingBoard board;

}
