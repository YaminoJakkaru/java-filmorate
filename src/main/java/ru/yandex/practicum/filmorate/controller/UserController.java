package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@RestController
public class UserController {
    private final InMemoryUserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public ArrayList<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @PostMapping("/users")
    public User create(@RequestBody @Email @NotNull User user) {
        return userStorage.createUser(user);
    }

    @PutMapping("/users")
    public User change(@RequestBody @Email @NotNull User user) {
        return userStorage.changeUser(user);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        return userStorage.getUser(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void makeFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.makeFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void breakFriends(@PathVariable int id, @PathVariable int friendId) {
        userService.breakFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public ArrayList<User> getFriends(@PathVariable int id) {
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public ArrayList<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getMutualFriends(id, otherId);
    }
}
