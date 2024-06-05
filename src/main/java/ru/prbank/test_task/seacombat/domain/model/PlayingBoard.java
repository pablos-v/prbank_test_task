package ru.prbank.test_task.seacombat.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "boards")
public class PlayingBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany // TODO @JoinColumn(name =  "ships", nullable  = false) ??
    private List<Ship> ships;
    @Column(name = "game")
    private Long gameId;
    @Column(name = "owner")
    private Long ownerId;
}
