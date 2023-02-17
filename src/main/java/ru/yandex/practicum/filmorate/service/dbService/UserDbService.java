package ru.yandex.practicum.filmorate.service.dbService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorageValidator;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;
import java.util.stream.Stream;

@Service
@Qualifier("UserDbService")
public class UserDbService implements UserService {

    private final UserStorage userStorage;
    private final UserStorageValidator userStorageValidator;
    private final UserValidator userValidator;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public UserDbService(@Qualifier("UserDbStorage") UserStorage userStorage,
                         @Qualifier("UserDbStorageValidator") UserStorageValidator userStorageValidator,
                         UserValidator userValidator) {
        this.userStorage = userStorage;
        this.userStorageValidator = userStorageValidator;
        this.userValidator = userValidator;
    }


    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User findUserById(int id) {
        return userStorage.findUserById(id);
    }

    @Override
    public User createUser(User user) {
        if (!userValidator.validate(user)) {
            LOG.warn("Валидация пользователя не пройдена");
            throw new ValidationException();
        }
        return userStorage.createUser(user);
    }

    @Override
    public User changeUser(User user) {
        if (!userValidator.validate(user)) {
            LOG.warn("Валидация пользователя не пройдена");
            throw new ValidationException();
        }
        return userStorage.changeUser(user);
    }

    @Override
    public List<User> getFriends(int id) {
        return userStorage.getFriends(id);
    }

    @Override
    public void makeFriends(int id, int friendId) {
        if (!userStorageValidator.usersIdValidate(id, friendId)) {
            LOG.warn("Пользователь не найден");
            throw new UserNotFoundException();
        }
        userStorage.makeFriends(id, friendId);
    }

    @Override
    public void breakFriends(int id, int friendId) {
        if (!userStorageValidator.usersIdValidate(id, friendId)) {
            LOG.warn("Пользователь не найден");
            throw new UserNotFoundException();
        }
        userStorage.breakFriends(id, friendId);
    }

    @Override
    public List<User> getMutualFriends(int id, int otherId) {
        if (!userStorageValidator.usersIdValidate(id, otherId)) {
            LOG.warn("Пользователь не найден");
            throw new UserNotFoundException();
        }
        return userStorage.getMutualFriends(id, otherId);
    }

    @Override
    public Stream<Film> getLikedFilms(int id) {
        return userStorage.getLikedFilms(id);
    }
}
