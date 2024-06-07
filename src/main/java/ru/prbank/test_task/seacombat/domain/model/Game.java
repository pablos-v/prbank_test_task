package ru.prbank.test_task.seacombat.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Сущность в БД. Описывает игру - содержит список игровых досок, очерёдность хода, и ID победителя.
 */
@Entity
@Data
@RequiredArgsConstructor
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "turn")
    private boolean turnOfSecondPlayer;
    @Column(name = "winner")
    private Long winnerId = -1L;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private List<PlayingBoard> boards;

    public Game(Long player1, Long player2) {
        this.boards = List.of(new PlayingBoard(player1), new PlayingBoard(player2));
    }

    /**
     * Меняет очерёдность хода игроков.
     */
    public void changeTurn() {
        turnOfSecondPlayer = !turnOfSecondPlayer;
    }

}
