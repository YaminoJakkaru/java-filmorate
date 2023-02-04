package ru.yandex.practicum.filmorate.service.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.rowMapper.UserMapper;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Qualifier("UserDbService")
public class UserDbService implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final JdbcTemplate jdbcTemplate;
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

    public UserDbService(JdbcTemplate jdbcTemplate, @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public List<User> getFriends(int id) {
        String queryUser = "select u.*,group_concat(uf.friend_id) as friends from users as u "
                + "left  join user_friend as uf on u.user_id=uf.user_id "
                + "where u.user_id in(select friend_id from user_friend where user_id=" + id + ") group by u.user_id";
        List<User> users = jdbcTemplate.query(queryUser, new UserMapper());
        return users;
    }


    @Override
    public void makeFriends(int id, int friendId) {
        String query = "insert into user_friend (user_id,friend_id) values (?,?)";
        checkIds(id, friendId);
        jdbcTemplate.update(query, id, friendId);
    }

    @Override
    public void breakFriends(int id, int friendId) {
        String query = "delete user_friend where user_id=? and friend_id=?";
        checkIds(id, friendId);
        jdbcTemplate.update(query, id, friendId);
    }

    @Override
    public List<User> getMutualFriends(int id, int otherId) {
        checkIds(id, otherId);
        String query = "select u.*,group_concat(uf.friend_id) as friends from users as u  "
                + "left  join user_friend as uf on u.user_id=uf.user_id where u.user_id in(select uf1.friend_id "
                + "from user_friend as uf1  inner join user_friend as uf2 on uf1.friend_id=uf2.friend_id"
                + " where uf1.user_id=" + id + " and uf2.user_id=" + otherId + ") group by u.user_id";
        return jdbcTemplate.query(query, new UserMapper());
    }

    private void checkIds(int id, int otherId) {
        String queryCheck = "select user_id from users where user_id=?";
        if (!jdbcTemplate.queryForRowSet(queryCheck, id).next() ||
                !jdbcTemplate.queryForRowSet(queryCheck, otherId).next()) {
            LOG.warn("Попытка  получить несуществующего пользователя");
            throw new UserNotFoundException();
        }
    }
}
