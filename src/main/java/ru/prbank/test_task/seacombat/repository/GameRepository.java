package ru.prbank.test_task.seacombat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.prbank.test_task.seacombat.domain.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

}
