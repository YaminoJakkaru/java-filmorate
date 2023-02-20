package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.rowMapper.FilmMapper;
import ru.yandex.practicum.filmorate.rowMapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private static final Logger LOG = LoggerFactory.getLogger(UserStorage.class);
    private final JdbcTemplate jdbcTemplate;
    SimpleJdbcInsert simpleJdbcInsertUser;
    private static final String BASE_FIND_QUERY = "select u.*,group_concat(uf.friend_id) as friends "
            + "from users as u  left  join user_friend as uf on u.user_id=uf.user_id";
    private static final String GROUP_BY_ID_CLAUSE = " group by u.user_id ";
    private static final String WHERE_ID_CLAUSE = " where u.user_id in (";

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
    }

    @Override
    public List<User> getAllUsers() {
        String queryUser = BASE_FIND_QUERY + GROUP_BY_ID_CLAUSE;
        List<User> users = jdbcTemplate.query(queryUser, new UserMapper());
        LOG.info("Запрошен список пользователей");
        return users;
    }

    @Override
    public User findUserById(int id) {

        String query = BASE_FIND_QUERY + WHERE_ID_CLAUSE + id + ")" + GROUP_BY_ID_CLAUSE;
        List<User> users = jdbcTemplate.query(query, new UserMapper());
        if (users.isEmpty()) {
            LOG.warn("Попытка  получить несуществующего пользователя");
            throw new UserNotFoundException();
        }
        return users.get(0);
    }

    @Override
    public User createUser(User user) {

        int userId = (int) simpleJdbcInsertUser.executeAndReturnKey(user.toMap());
        user.setId(userId);
        LOG.info("Добавлен пользователь");
        return user;
    }

    @Override
    public User changeUser(User user) {
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

    @Override
    public List<User> getFriends(int id) {
        String queryUser = BASE_FIND_QUERY + WHERE_ID_CLAUSE
                + " select friend_id from user_friend where user_id=" + id + ")" + GROUP_BY_ID_CLAUSE;
        return jdbcTemplate.query(queryUser, new UserMapper());
    }

    @Override
    public void makeFriends(int id, int friendId) {
        String query = "insert into user_friend (user_id,friend_id) values (?,?)";
        jdbcTemplate.update(query, id, friendId);
    }

    @Override
    public void breakFriends(int id, int friendId) {
        String query = "delete user_friend where user_id=? and friend_id=?";
        jdbcTemplate.update(query, id, friendId);
    }

    @Override
    public List<User> getMutualFriends(int id, int otherId) {
        String query = BASE_FIND_QUERY + WHERE_ID_CLAUSE + " select uf1.friend_id "
                + "from user_friend as uf1  inner join user_friend as uf2 on uf1.friend_id=uf2.friend_id "
                + " where uf1.user_id=" + id + " and uf2.user_id=" + otherId + ")" + GROUP_BY_ID_CLAUSE;
        return jdbcTemplate.query(query, new UserMapper());
    }

    @Override
    public List<Film> getRecommendFilms(int id) {
        String query = "SELECT * " +
                "FROM film" +
                "WHERE film_id IN" +
                "    (SELECT DISTINCT film_id" +
                "     FROM film_likes" +
                "     WHERE film_id NOT IN" +
                "         (SELECT film_id" +
                "          FROM film_likes" +
                "          WHERE user_id = ?)" +
                "       AND user_id IN" +
                "         (SELECT user_id AS _user_id," +
                "          FROM film_likes" +
                "          WHERE film_id IN" +
                "              (SELECT film_id" +
                "               FROM likes" +
                "               WHERE user_id = ?)" +
                "          GROUP BY _user_id" +
                "          ORDER BY COUNT (film_id) DESC" +
                "          LIMIT 10))";
        return jdbcTemplate.query(query, new FilmMapper());
    }
}
