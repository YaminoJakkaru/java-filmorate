package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.rowMapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Qualifier("UserDbService")
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getFriends(int id) {
        return userStorage.getFriends(id);
    }

    public void makeFriends(int id, int friendId) {
        userStorage.makeFriends(id, friendId);
    }

    public void breakFriends(int id, int friendId) {
        userStorage.breakFriends(id, friendId);
    }

    public List<User> getMutualFriends(int id, int otherId) {
        return userStorage.getMutualFriends(id, otherId);
    }
}
