package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.reader.Reader;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;

@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserStorage.class);
    private final UserValidator userValidator;
    private final JdbcTemplate jdbcTemplate;
    private final Reader reader;

    public UserDbStorage(UserValidator userValidator, JdbcTemplate jdbcTemplate, Reader reader) {
        this.userValidator = userValidator;
        this.jdbcTemplate = jdbcTemplate;
        this.reader = reader;
    }

    @Override
    public List<User> getAllUsers() {
        String queryUser="select * from users";

        List<User> users = jdbcTemplate.query(queryUser, reader::readUser);
        for (User user : users) {
            String queryFriends="select friend_id from user_friend where user_id=" + user.getId();
            jdbcTemplate.queryForList(queryFriends, Integer.class).forEach(user::addFriend);
        }
        log.warn("Запрошен список пользователей");
        return users;
    }

    @Override
    public User findUserById(int id) {
        try {
            String query="select * from users where user_id=" + id;
            return jdbcTemplate.query(query, reader::readUser).get(0);
        } catch (IndexOutOfBoundsException e) {
            log.warn("Попытка  получить несуществующего пользователя");
            throw new UserNotFoundException();
        }
    }

    @Override
    public User createUser(User user) {
        if (!userValidator.validate(user)) {
            log.warn("Валидация пользователя не пройдена");
            throw new ValidationException();
        }
        SimpleJdbcInsert simpleJdbcInsertFilm = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        int userId = (int) simpleJdbcInsertFilm.executeAndReturnKey(user.toMap());
        user.setId(userId);
        log.info("Добавлен пользователь");
        return findUserById(userId);
    }

    @Override
    public User changeUser(User user) {
        String queryCheck="select user_id from users where user_id=" + user.getId();
        if (!jdbcTemplate.queryForRowSet(queryCheck).next()) {
            log.warn("Попытка изменить несуществующего пользователя");
            throw new UserNotFoundException();
        }
        if (!userValidator.validate(user)) {
            log.warn("Валидация пользователя не пройдена");
            throw new ValidationException();
        }
        String sqlQuery = "update users set " +
                "email = ?,  login = ?,name=?,birthday=? " +
                "where user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.info("Данные пользователя обновлен");
        return findUserById(user.getId());

    }
}
