package ru.prbank.test_task.seacombat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.prbank.test_task.seacombat.domain.model.Player;
import ru.prbank.test_task.seacombat.repository.PlayerRepository;

@SpringBootApplication
public class SeaCombatApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeaCombatApplication.class, args);
    }

}
