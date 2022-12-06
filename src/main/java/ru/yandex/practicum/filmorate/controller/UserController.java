package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

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
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        return userStorage.createUser(user);
    }

    @PutMapping("/users")
    public User change(@RequestBody User user) {
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
    public List<User> getFriends(@PathVariable int id) {
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getMutualFriends(id, otherId);
    }
}
