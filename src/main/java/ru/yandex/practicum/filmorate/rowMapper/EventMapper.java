package ru.yandex.practicum.filmorate.rowMapper;

import org.springframework.jdbc.core.RowMapper;

import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(resultSet.getInt("event_id"))
                .userId(resultSet.getInt("user_id"))
                .entityId(resultSet.getInt("entity_id"))
                .eventType(resultSet.getString("event_type_name"))
                .operation(resultSet.getString("operation_name"))
                .timestamp(resultSet.getTimestamp("timestamp").getTime())
                .build();
    }
}