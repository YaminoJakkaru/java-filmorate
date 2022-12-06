package ru.yandex.practicum.filmorate.id;

import org.springframework.stereotype.Component;

@Component
public class Id {
    private int Id = 0;


    public int getNewId() {
        Id++;
        return Id;
    }


}
