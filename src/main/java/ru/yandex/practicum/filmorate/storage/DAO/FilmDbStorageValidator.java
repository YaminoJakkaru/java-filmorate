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
    public boolean filmIdValidate(int id) {
        String queryCheck = "select film_id from film where film_id = " + id;
        return jdbcTemplate.queryForRowSet(queryCheck).next();
    }
}
