package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventService {

    List<Event> getFeed(int id);
}
