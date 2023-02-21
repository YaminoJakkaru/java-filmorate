package ru.yandex.practicum.filmorate.service.dbService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.UserStorageValidator;

import java.util.List;


@Service
public class EventDbService implements EventService {
    private final EventStorage eventStorage;
    private final UserStorageValidator userStorageValidator;
    private static final Logger LOG = LoggerFactory.getLogger(EventService.class);

    @Autowired
    public EventDbService(@Qualifier("EventDbStorage") EventStorage eventStorage,
                          @Qualifier("UserDbStorageValidator") UserStorageValidator userStorageValidator) {
        this.eventStorage = eventStorage;
        this.userStorageValidator = userStorageValidator;
    }

    @Override
    public List<Event> getFeed(int id) {
        if (!userStorageValidator.userIdValidate(id)) {
            LOG.warn("Пользователь не найден");
            throw new UserNotFoundException();
        }
        return eventStorage.getFeed(id);
    }
}
