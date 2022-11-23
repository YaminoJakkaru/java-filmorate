package ru.yandex.practicum.filmorate.id;

public class Id {
    private int id = 0;

    public int getNewId() {
        id++;
        return id;
    }
}
