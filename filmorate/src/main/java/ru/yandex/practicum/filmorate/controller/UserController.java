package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.id.Id;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import javax.validation.ValidationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, User> users = new HashMap<>();
    UserValidator userValidator= new UserValidator();
    Id id=new Id();

    @GetMapping("/users")
    public ArrayList<User> users() {
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User create(@RequestBody @Email @NotNull User user) {
        if(userValidator.validate(user)) {
            if(user.getName()==null||user.getName().isBlank()){
                user.setName(user.getLogin());
            }
            user.setId(id.getNewId());
            users.put(user.getId(), user);
            log.info("Добавлен пользователь");
            return user;
        }
        log.warn("Валидация пользователя не пройдена");
        throw new ValidationException();
    }

    @PutMapping("/users")
    public User change(@RequestBody @Email @NotNull User user) {
        if(userValidator.validate(user)) {
            if(!users.containsKey(user.getId())){
                throw new ValidationException();
            }
        users.put(user.getId(), user);
            log.info("Пользователь обновлен");
        return user;
        }
        log.warn("Валидация пользователя не пройдена");
        throw new ValidationException();
    }
}
