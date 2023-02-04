package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import ru.yandex.practicum.filmorate.rowMapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Component
@Qualifier("GenreDbStorage")
public class GenreDbStorage implements GenreStorage {
    private static final Logger LOG = LoggerFactory.getLogger(GenreStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre findGenreById(int id) {
        String query = "select * from genre where genre_id=" + id;
        List<Genre> genres = jdbcTemplate.query(query, new GenreMapper());
        if (genres.isEmpty()) {
            LOG.warn("Попытка  получить несуществующий рейтинг");
            throw new NotFoundException();
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> getAllGenre() {
        String query = "select * from genre";
        return jdbcTemplate.query(query, new GenreMapper());
    }
}
