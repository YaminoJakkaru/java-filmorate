package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorageValidator;

@Component
@Qualifier("UserDbStorageValidator")
public class UserDbStorageValidator implements UserStorageValidator {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorageValidator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean usersIdValidate(int id, int otherId) {
        String queryCheck = " select count (user_id) from users where user_id in (" + id + "," + otherId + ")";
        return jdbcTemplate.queryForObject(queryCheck, Integer.class) == 2;

    }

    @Override
    public boolean userIdValidate(int id) {
        String queryCheck = "select user_id from users where user_id=" + id;
        return jdbcTemplate.queryForRowSet(queryCheck).next();
    }
}
