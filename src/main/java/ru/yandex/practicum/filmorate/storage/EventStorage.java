package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.enums.EntityType;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventStorage {

    void createEvent(int userId, int entityId, EntityType entityType, EventType eventType, Operation operation);

    List<Event> getFeed(int userId);
}
