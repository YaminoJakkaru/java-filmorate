package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getFriends(int id) {
        ArrayList<User> friends = new ArrayList<>();
        for (int friend : userStorage.getUser(id).getFriends()) {
            friends.add(userStorage.getUser(friend));
        }
        return friends;
    }

    public void makeFriends(int id, int friendId) {
        if (userStorage.getUser(friendId) != null) {
            userStorage.getUser(id).addFriend(friendId);
            userStorage.getUser(friendId).addFriend(id);
            log.info("Создана дружба");
            return;
        }
        throw new UserNotFoundException();
    }

    public void breakFriends(int id, int friendId) {
        userStorage.getUser(id).deleteFriend(friendId);
        userStorage.getUser(friendId).deleteFriend(id);
        log.info("Дружба удалена");
    }

    public List<User> getMutualFriends(int id, int otherId) {
        ArrayList<User> mutualFriends = new ArrayList<>();
        for (int friend : userStorage.getUser(id).getFriends()) {
            if (userStorage.getUser(otherId).getFriends().contains(friend)) {
                mutualFriends.add(userStorage.getUser(friend));
            }
        }
        return mutualFriends;
    }

}
