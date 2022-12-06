package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;


@Data
public class Film {

    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    private final Set<Integer> likes=new HashSet<>();

    public void addLikes(int id){
        likes.add(id);
    }
    public void deleteLikes(int id){
        likes.remove(id);
    }
}
