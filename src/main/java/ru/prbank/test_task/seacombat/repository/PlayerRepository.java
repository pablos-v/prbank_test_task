package ru.prbank.test_task.seacombat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.prbank.test_task.seacombat.domain.model.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

}
