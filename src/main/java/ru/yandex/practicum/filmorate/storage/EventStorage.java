package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventStorage {

    void createEvent(int userId,int entityId, int eventType,int operation);

    List<Event> getFeed(int userId);
}
