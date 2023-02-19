package ru.yandex.practicum.filmorate.storage.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.rowMapper.EventMapper;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.List;

@Component
@Qualifier("EventDbStorage")
public class EventDbStorage implements EventStorage {

    private final JdbcTemplate jdbcTemplate;

    public EventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public void createEvent(int userId, int entityId, int eventType, int operation) {
        String query = "insert into event(user_id, entity_id, event_type_id, operation_id, timestamp) "
                + "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, userId, entityId, eventType, operation,
                new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public List<Event> getFeed(int userId) {
        String query = "select e.event_id,e.user_id, e.entity_id, et.name as event_type_name,"
                + " o.name as operation_name, e.timestamp from event as e "
                + "left join event_type as et on e.event_type_id=et.event_type_id "
                + "left join operation as o on e.operation_id=o.operation_id where user_id=" + userId
                + " order by timestamp";
        return jdbcTemplate.query(query, new EventMapper());
    }
}
