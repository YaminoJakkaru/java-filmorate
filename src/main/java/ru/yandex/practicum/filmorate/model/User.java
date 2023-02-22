package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.*;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday ;

    private final Set<Integer> friends=new HashSet<>();
    private final Set<Integer> unconfirmedFriends=new HashSet<>();
    public void addFriend(int id){
        friends.add(id);
    }
    public void deleteFriend(int id){
        friends.remove(id);
    }
    public void addUnconfirmedFriend(int id){
        unconfirmedFriends.add(id);
    }
    public void deleteUnconfirmedFriend(int id){
        unconfirmedFriends.remove(id);
    }

    public Map<String, Object> toMap() {
        return Map.of("email", email,
                "login", login,
                "name", name,
                "birthday", birthday);
    }
}
