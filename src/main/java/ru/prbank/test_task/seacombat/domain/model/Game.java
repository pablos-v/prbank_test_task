package ru.prbank.test_task.seacombat.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name  = "is_over")
    private boolean isOver;
    @Column(name  = "turn")
    private boolean TurnOfSecondPlayer;

    @OneToMany // TODO  @JoinColumn(name  = "players", nullable  = false)??
    private final List<PlayingBoard> boards;

    public void changeTurn(){
        TurnOfSecondPlayer = !TurnOfSecondPlayer;
    }

}
