package ru.yandex.practicum.filmorate.storage.InMemory;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.id.Id;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final Id id;
    private final UserValidator userValidator;
    private static final Logger log = LoggerFactory.getLogger(UserStorage.class);

    @Autowired
    public InMemoryUserStorage( UserValidator userValidator) {
        this.id = new Id();
        this.userValidator = userValidator;
    }

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @Override
    public User findUserById(int id){
        if(!users.containsKey(id)){
            throw new UserNotFoundException();
        }
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        if (userValidator.validate(user)) {
            user.setId(id.getNewId());
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Добавлен пользователь");
            return user;
        }
        log.warn("Валидация пользователя не пройдена");
        throw new ValidationException();
    }

    @Override
    public User changeUser(User user) {
        if (userValidator.validate(user)) {
            if (!users.containsKey(user.getId())) {
                log.warn("Попытка изменить несуществующего пользователя");
                throw new NullPointerException();
            }
            users.put(user.getId(), user);
            log.info("Пользователь изменен");
            return user;
        }
        log.warn("Валидация пользователя не пройдена");
        throw new ValidationException();
    }
}
