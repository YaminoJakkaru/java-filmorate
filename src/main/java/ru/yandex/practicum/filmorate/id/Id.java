package ru.yandex.practicum.filmorate.id;

import org.springframework.stereotype.Component;

@Component
public class Id {
    private int id = 0;


    public int getNewId() {
        id++;
        return id;
    }


}
