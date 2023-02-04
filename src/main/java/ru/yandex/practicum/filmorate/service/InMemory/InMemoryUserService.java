package ru.yandex.practicum.filmorate.service.InMemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("InMemoryUserService")
public class InMemoryUserService implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryUserService.class);
    @Qualifier("InMemoryUserStorage")
    private final UserStorage userStorage;

    @Autowired
    public InMemoryUserService(@Qualifier("InMemoryUserStorage")UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getFriends(int id) {
        ArrayList<User> friends = new ArrayList<>();
        for (int friend : userStorage.findUserById(id).getFriends()) {
            friends.add(userStorage.findUserById(friend));
        }
        return friends;
    }

    public void makeFriends(int id, int friendId) {
        if (userStorage.findUserById(friendId) != null) {
            userStorage.findUserById(id).addFriend(friendId);
            userStorage.findUserById(friendId).addFriend(id);
            LOG.info("Создана дружба");
            return;
        }
        throw new UserNotFoundException();
    }

    public void breakFriends(int id, int friendId) {
        userStorage.findUserById(id).deleteFriend(friendId);
        userStorage.findUserById(friendId).deleteFriend(id);
        LOG.info("Дружба удалена");
    }

    public List<User> getMutualFriends(int id, int otherId) {
        ArrayList<User> mutualFriends = new ArrayList<>();
        for (int friend : userStorage.findUserById(id).getFriends()) {
            if (userStorage.findUserById(otherId).getFriends().contains(friend)) {
                mutualFriends.add(userStorage.findUserById(friend));
            }
        }
        return mutualFriends;
    }

}
