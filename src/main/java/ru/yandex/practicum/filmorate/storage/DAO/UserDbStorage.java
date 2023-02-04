package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.rowMapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;

@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private static final Logger LOG = LoggerFactory.getLogger(UserStorage.class);
    private final UserValidator userValidator;
    private final JdbcTemplate jdbcTemplate;

    SimpleJdbcInsert simpleJdbcInsertUser;

    public UserDbStorage(UserValidator userValidator, JdbcTemplate jdbcTemplate) {
        this.userValidator = userValidator;
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
    }

    @Override
    public List<User> getAllUsers() {
        String queryUser = "select u.*,group_concat(uf.friend_id) as friends "
                + "from users as u  left  join user_friend as uf on u.user_id=uf.user_id group by u.user_id";
        List<User> users = jdbcTemplate.query(queryUser, new UserMapper());
        LOG.info("Запрошен список пользователей");
        return users;
    }

    @Override
    public User findUserById(int id) {

        String query = "select u.*,group_concat(uf.friend_id) as friends from users as u  "
                + "left  join user_friend as uf on u.user_id=uf.user_id where u.user_id=" + id
                + " group by u.user_id";
        List<User> users = jdbcTemplate.query(query, new UserMapper());
        if (users.isEmpty()) {
            LOG.warn("Попытка  получить несуществующего пользователя");
            throw new UserNotFoundException();
        }
        return users.get(0);
    }

    @Override
    public User createUser(User user) {
        if (!userValidator.validate(user)) {
            LOG.warn("Валидация пользователя не пройдена");
            throw new ValidationException();
        }
        int userId = (int) simpleJdbcInsertUser.executeAndReturnKey(user.toMap());
        user.setId(userId);
        LOG.info("Добавлен пользователь");
        return user;
    }

    @Override
    public User changeUser(User user) {
        if (!userValidator.validate(user)) {
            LOG.warn("Валидация пользователя не пройдена");
            throw new ValidationException();
        }
        String sqlQuery = "update users set " +
                "email = ?,  login = ?,name=?,birthday=? " +
                "where user_id = ?";
        int changes = jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        if (changes == 0) {
            LOG.warn("Попытка изменить несуществующего пользователя");
            throw new UserNotFoundException();
        }
        LOG.info("Данные пользователя обновлен");
        return findUserById(user.getId());

    }
}
