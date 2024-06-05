package ru.prbank.test_task.seacombat.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name  = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name   = "name", nullable = false)
    private String name;

    private List<Game> games;


}
