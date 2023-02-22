package ru.yandex.practicum.filmorate.storage.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.UserStorageValidator;

@Component
public class UserDbStorageValidator implements UserStorageValidator {
    private final JdbcTemplate jdbcTemplate;
    private static final int REQUIRED_QUANTITY = 2;

    public UserDbStorageValidator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean usersIdValidate(int id, int otherId) {
        String queryCheck = " select count (user_id) from users where user_id in (" + id + "," + otherId + ")";
        return jdbcTemplate.queryForObject(queryCheck, Integer.class) == REQUIRED_QUANTITY;

    }

    @Override
    public boolean userIdValidate(int id) {
        String queryCheck = "select user_id from users where user_id=" + id;
        return jdbcTemplate.queryForRowSet(queryCheck).next();
    }
}
