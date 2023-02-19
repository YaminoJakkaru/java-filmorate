package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final EventService eventService;

    @Autowired
    public UserController(@Qualifier("UserDbService") UserService userService,
                          @Qualifier("EventDbService") EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User change(@RequestBody User user) {
        return userService.changeUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userService.findUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void makeFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.makeFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void breakFriends(@PathVariable int id, @PathVariable int friendId) {
        userService.breakFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}/feed")
    public List<Event> getFeed(@PathVariable int id){
        return eventService.getFeed(id);
    }
}
