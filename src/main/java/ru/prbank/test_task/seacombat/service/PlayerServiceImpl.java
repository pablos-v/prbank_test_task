package ru.prbank.test_task.seacombat.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prbank.test_task.seacombat.domain.model.Player;
import ru.prbank.test_task.seacombat.repository.PlayerRepository;
@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository repository;

    @Override
    public boolean isPlayerExists(Long playerId) {
        return repository.existsById(playerId);
    }
}
