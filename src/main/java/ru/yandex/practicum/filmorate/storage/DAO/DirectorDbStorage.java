package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.rowMapper.DirectorMapper;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Component
@Qualifier("DirectorDbStorage")
public class DirectorDbStorage implements DirectorStorage {
    private static final Logger LOG = LoggerFactory.getLogger(DirectorStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Director findDirectorById(int id) {
        String query = "select * from director where director_id=" + id;
        List<Director> director = jdbcTemplate.query(query, new DirectorMapper());
        if (director.isEmpty()) {
            LOG.warn("Попытка  получить несуществующего режиссера");
            throw new NotFoundException();
        }
        return director.get(0);
    }

    @Override
    public List<Director> getAllDirectors() {
        String query = "select * from director";
        return jdbcTemplate.query(query, new DirectorMapper());
    }
}
