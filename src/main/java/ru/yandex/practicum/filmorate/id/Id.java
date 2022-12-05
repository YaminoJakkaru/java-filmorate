package ru.yandex.practicum.filmorate.id;

import org.springframework.stereotype.Component;

@Component
public class Id {
    private int filmId = 0;
    private int userId = 0;

    public int getNewFilmId() {
        filmId++;
        return filmId;
    }

    public int getNewUserId() {
        userId++;
        return userId;
    }
}
