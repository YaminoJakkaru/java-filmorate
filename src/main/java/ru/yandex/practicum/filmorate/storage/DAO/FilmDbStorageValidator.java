package ru.yandex.practicum.filmorate.storage.DAO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FilmStorageValidator;

@Component
@Qualifier("FilmDbStorageValidator")
public class FilmDbStorageValidator implements FilmStorageValidator {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorageValidator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean filmLikeValidate(int filmId, int userId) {
        String queryCheck= "select film_likes_id from film_likes where film_likes_id=" + filmId + userId;
        return jdbcTemplate.queryForRowSet(queryCheck).next();
    }
}
