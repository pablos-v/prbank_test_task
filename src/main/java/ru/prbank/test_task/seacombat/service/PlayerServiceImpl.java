package ru.prbank.test_task.seacombat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prbank.test_task.seacombat.repository.PlayerRepository;

/**
 * Реализация сервиса для операций с игроками.
 */
@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository repository;

     /**
     * Реализация метода {@link PlayerService#isPlayerExists(Long)}
     * См. описание метода в интерфейсе {@link PlayerService}
     */
    @Override
    public boolean isPlayerExists(Long playerId) {
        return repository.existsById(playerId);
    }
}
