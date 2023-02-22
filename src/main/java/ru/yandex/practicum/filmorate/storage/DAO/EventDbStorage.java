package ru.yandex.practicum.filmorate.storage.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.EntityType;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.rowMapper.EventMapper;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.util.List;

@Component
public class EventDbStorage implements EventStorage {

    private final JdbcTemplate jdbcTemplate;

    public EventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public void createEvent(int userId, int entityId, EntityType entityType, EventType eventType, Operation operation) {
        String query = "insert into event(user_id, entity_id,entity_type, event_type, operation) "
                + "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, userId, entityId,entityType.toString(), eventType.toString(), operation.toString());
    }

    @Override
    public List<Event> getFeed(int userId) {
        String query = "select * from event  where user_id=" + userId + " order by created_at asc";
        return jdbcTemplate.query(query, new EventMapper());
    }
}
