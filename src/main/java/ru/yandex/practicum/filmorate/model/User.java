package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.*;

import lombok.Data;
import lombok.Value;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    private final Set<Integer> friends=new HashSet<>();
    public void addFriend(int id){
        friends.add(id);
    }
    public void deleteFriend(int id){
        friends.remove(id);
    }
}
