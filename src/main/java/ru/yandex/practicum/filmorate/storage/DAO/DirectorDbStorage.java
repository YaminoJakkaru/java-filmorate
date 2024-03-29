package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.rowMapper.DirectorMapper;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Component
public class DirectorDbStorage implements DirectorStorage {
    private static final Logger LOG = LoggerFactory.getLogger(DirectorStorage.class);

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsertDirector;

    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsertDirector = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("director")
                .usingGeneratedKeyColumns("director_id");
    }

    @Override
    public Director createDirector(Director director) {
        int directorId = (int) simpleJdbcInsertDirector.executeAndReturnKey(director.toMap());
        director.setId(directorId);
        LOG.info("Добавлен режиссер");
        return director;
    }

    @Override
    public Director findDirectorById(int id) {
        String query = "select * from director where director_id=" + id;
        return jdbcTemplate.query(query, DirectorMapper.INSTANCE)
                .stream()
                .findFirst()
                .orElseThrow(() -> {
                    LOG.warn("Попытка получить несуществующего режиссера");
                    return new NotFoundException();
                });
    }

    @Override
    public List<Director> getAllDirectors() {
        String query = "select * from director";
        return jdbcTemplate.query(query, DirectorMapper.INSTANCE);
    }

    @Override
    public Director changeDirector(Director director) {
        String sqlQuery = "update director set " +
                "name = ? " +
                "where director_id = ?";

        int changes = jdbcTemplate.update(sqlQuery, director.getName(), director.getId());

        if (changes == 0) {
            LOG.warn("Попытка изменить несуществующего режиссера");
            throw new NotFoundException();
        }
        LOG.info("Данные режиссера обновлены");
        return director;
    }

    @Override
    public void deleteDirector(int id) {
        String query = "delete from director where director_id = ?";
        jdbcTemplate.update(query, id);
        LOG.info("Режиссер с id=" + id + " был удален");
    }
}
